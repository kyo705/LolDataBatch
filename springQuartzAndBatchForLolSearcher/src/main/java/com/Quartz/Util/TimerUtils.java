package com.Quartz.Util;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.Quartz.Timer.Timer;


public final class TimerUtils {

	private TimerUtils() {}
	
	public static JobDetail buildJobDetail(final Class<? extends Job> jobclass, final Timer timerInfo) {
		final JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(jobclass.getSimpleName(), timerInfo);
		
		return JobBuilder
				.newJob(jobclass)
				.withIdentity(jobclass.getSimpleName())
				.setJobData(jobDataMap)
				.build();
	}
	
	public static Trigger buildTriggerWithSimpleSchedule(final Class<? extends Job> jobclass, final Timer timerInfo) {
		SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
				.simpleSchedule().withIntervalInMilliseconds(timerInfo.getRepeatIntervalMs());
		
		if(timerInfo.isRunForever()) {
			simpleScheduleBuilder.repeatForever();
		}else {
			simpleScheduleBuilder.withRepeatCount(timerInfo.getTotalFireCount()-1);
		}
		
		return TriggerBuilder
				.newTrigger()
				.withIdentity(jobclass.getSimpleName())
				.withSchedule(simpleScheduleBuilder)
				.startAt(new Date(System.currentTimeMillis()+timerInfo.getInitialOffsetMs()))
				.build();
	}
}
