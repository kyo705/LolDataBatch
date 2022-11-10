package com.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.quartz.job.ChampStaticQuartzJob;
import com.quartz.job.MatchesQuartzJob;
import com.quartz.service.SchedulerService;
import com.quartz.timer.Timer;


@Service
public class RunningSchedulerService {
	private SchedulerService schedulerService;
	
	public RunningSchedulerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}
	
	@PostConstruct
	public void runChampStaticBatchJob() {
		Timer champStaticTimer = new Timer();
		champStaticTimer.setRunForever(true);
		champStaticTimer.setTotalFireCount(5);
		champStaticTimer.setCallbackData("champStaticTimer1");
		champStaticTimer.setInitialOffsetMs(0);
		champStaticTimer.setRepeatIntervalMs(1000*60*60*24);
		
		schedulerService.schedule(ChampStaticQuartzJob.class, champStaticTimer);
		System.out.println("controllerø°∞‘ ∫∏≥ª¡‹");
	}
	
	
	public void runMatchesBatchJob() {
		Timer matchesTimer = new Timer();
		matchesTimer.setRunForever(true);
		matchesTimer.setTotalFireCount(0);
		matchesTimer.setCallbackData("matchesTimer1");
		matchesTimer.setInitialOffsetMs(0);
		matchesTimer.setRepeatIntervalMs(2000*60);
		
		schedulerService.schedule(MatchesQuartzJob.class, matchesTimer);
	}
}
