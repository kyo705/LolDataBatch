package com.Quartz.service.TimerService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Quartz.Timer.Timer;
import com.Quartz.Util.TimerUtils;


@Service
public class SchedulerService {
	private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

	private final Scheduler scheduler;
	
	@Autowired
	public SchedulerService(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void schedule(Class<? extends Job> jobclass, Timer timer) {
		JobDetail jobDetail = TimerUtils.buildJobDetail(jobclass, timer);
		Trigger trigger = TimerUtils.buildTriggerWithSimpleSchedule(jobclass, timer);
		
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@PostConstruct
	public void init() {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@PreDestroy
	public void predestroy() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}
}
