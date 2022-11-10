package com.quartz.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.quartz.timer.Timer;
import com.quartz.util.TimerUtils;


@Service
public class SchedulerService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Scheduler scheduler;
	
	public SchedulerService(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	
	@PostConstruct
	public void init() {
		try {
			logger.info("스케줄러 : {} 열림", scheduler.toString());
			scheduler.start();
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	@PreDestroy
	public void predestroy() {
		try {
			logger.info("스케줄러 : {} 종료", scheduler.toString());
			scheduler.shutdown();
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	
	public void schedule(Class<? extends Job> jobclass, Timer timer) {
		JobDetail jobDetail = TimerUtils.buildJobDetail(jobclass, timer);
		Trigger trigger = TimerUtils.buildTriggerWithSimpleSchedule(jobclass, timer);
		
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
