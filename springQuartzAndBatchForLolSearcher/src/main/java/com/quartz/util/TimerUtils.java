package com.quartz.util;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.JobDetailImpl;

import com.quartz.timer.Timer;


public final class TimerUtils {

	private TimerUtils() {}
	
	public static JobDetail buildJobDetail(final Class<? extends Job> jobclass, final Timer timerInfo) {
		JobKey jobKey = new JobKey(timerInfo.getCallbackData());
		
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(jobKey.getName(), timerInfo);
		return (JobDetailImpl)JobBuilder
				.newJob(jobclass)
				.withIdentity(jobKey)
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
				.withIdentity(timerInfo.getCallbackData())
				.withSchedule(simpleScheduleBuilder)
				.startAt(new Date(System.currentTimeMillis()+timerInfo.getInitialOffsetMs()))
				.build();
	}
}
