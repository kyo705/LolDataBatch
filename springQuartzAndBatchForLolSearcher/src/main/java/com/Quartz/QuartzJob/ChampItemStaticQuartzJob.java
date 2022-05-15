package com.Quartz.QuartzJob;

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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class ChampItemStaticQuartzJob implements Job {
	private static final Logger log = LoggerFactory.getLogger(ChampItemStaticQuartzJob.class);
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info(ChampItemStaticQuartzJob.class.getSimpleName() + " ½ÇÇà");
		
		org.springframework.batch.core.Job champItemStaticBatchJob = 
				(org.springframework.batch.core.Job) applicationContext.getBean("champItemStaticJob");
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("currentTimeStamp", System.currentTimeMillis())
				.addLong("queueId", 420L)
				.toJobParameters();
		
		try {
			jobLauncher.run(champItemStaticBatchJob, jobParameters);
		} catch (BeansException | JobExecutionAlreadyRunningException | JobRestartException
				| JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
			log.error(e.getMessage(),e);
		}
	}

}
