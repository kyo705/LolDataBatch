package com.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ChampionQuartzJob implements Job {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final long queueId = 420L;
	private final long seasonId = 12L;
	
	@Qualifier("championJob")
	@Autowired
	private org.springframework.batch.core.Job champStaticBatchJob;
	@Autowired
	private JobLauncher jobLauncher;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("{} 실행", this.getClass().getSimpleName());
		
		JobParameters jobParameters = new JobParametersBuilder()
									.addLong("currentTimeStamp", System.currentTimeMillis())
									.addLong("queueId", queueId)
									.addLong("seasonId", seasonId)
									.toJobParameters();
		
		try {
			JobExecution jobExecution = jobLauncher.run(champStaticBatchJob, jobParameters);
			
			if(jobExecution.getExitStatus()==ExitStatus.STOPPED||
					jobExecution.getExitStatus()==ExitStatus.FAILED) {
				log.error("배치 잡 : {} 에러 발생", champStaticBatchJob.getClass().getSimpleName());
				//배치 잡 에러사항을 email이나 문자로 관리자에게 전달하는 로직
				
				//에러 발생으로 쿼츠 실행 스레드 종료
				throw new JobExecutionException();
			}
		} catch (BeansException | 
				JobExecutionAlreadyRunningException | 
				JobRestartException | 
				JobInstanceAlreadyCompleteException | 
				JobParametersInvalidException e) {
			log.error(e.getMessage(),e);
		}
	}
}
