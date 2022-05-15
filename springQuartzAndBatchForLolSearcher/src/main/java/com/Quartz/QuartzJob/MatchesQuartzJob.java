package com.Quartz.QuartzJob;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class MatchesQuartzJob implements Job{
	private static final Logger log = LoggerFactory.getLogger(MatchesQuartzJob.class);
	
	public Map<String, String> matchIds = new ConcurrentHashMap<>();
	
	public Map<String, String> puuIds = new ConcurrentHashMap<>();
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info(MatchesQuartzJob.class.getSimpleName() + " 실행");
		
		if(matchIds.size()==0) {
			if(puuIds.size()==0) {
				org.springframework.batch.core.Job puuIdsJob = 
						(org.springframework.batch.core.Job) applicationContext.getBean("puuIdsJob");
				
				JobParameters jobParameters = new JobParametersBuilder()
						.addLong("currentTimeStamp", System.currentTimeMillis())
						.toJobParameters();
				
				try {
					jobLauncher.run(puuIdsJob, jobParameters);
				} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
						| JobParametersInvalidException e) {
					e.printStackTrace();
				}
			}else {
				Iterator<Map.Entry<String, String>> puuidIterator = puuIds.entrySet().iterator();
				
				org.springframework.batch.core.Job matchListJob = 
						(org.springframework.batch.core.Job) applicationContext.getBean("matchListJob");
				
				int count = 0;
				while(puuidIterator.hasNext()) {
					if(count>=20) break; //riot api로 부터 얻는 정보 제한 때문(2분에 최대 100회 요청가능)
					
					Entry<String,String> cur = puuidIterator.next();
					
					JobParameters jobParameters = new JobParametersBuilder()
							.addString("puuId", cur.getKey())
							.addString("lastMatchId", cur.getValue())
							.toJobParameters();
					
					try {
						jobLauncher.run(matchListJob, jobParameters);
					} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
							| JobParametersInvalidException | DataIntegrityViolationException e) {
						log.error(e.getMessage(),e);
						puuidIterator.remove();
					} catch (WebClientResponseException e) {
						log.error(e.getMessage() + " 배치 잡 실행 종료",e);
						break;
					}
					
					count++;
				}
			}
		}else {
			Iterator<Map.Entry<String, String>> iter = matchIds.entrySet().iterator();
			
			org.springframework.batch.core.Job matchesJob = 
					(org.springframework.batch.core.Job) applicationContext.getBean("matchesJob");
			
			int count = 0;
			while(iter.hasNext()) {
				if(count>=20) break; //riot api로 부터 얻는 정보 제한 때문(2분에 최대 100회 요청가능)
				
				Entry<String,String> cur = iter.next();
				
				JobParameters jobParameters = new JobParametersBuilder()
						.addString("matchId", cur.getKey())
						.toJobParameters();
				
				try {
					jobLauncher.run(matchesJob, jobParameters);
				} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
						| JobParametersInvalidException | DataIntegrityViolationException e) {
					log.error(e.getMessage(),e);
					iter.remove();
				} catch (WebClientResponseException e) {
					log.error(e.getMessage() + " 배치 잡 실행 종료",e);
					break;
				}
				
				count++;
			}
		}
	}

}
