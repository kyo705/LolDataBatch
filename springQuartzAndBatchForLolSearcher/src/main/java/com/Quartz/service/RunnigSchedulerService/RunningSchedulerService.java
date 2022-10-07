package com.Quartz.service.RunnigSchedulerService;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Quartz.QuartzJob.ChampStaticQuartzJob;
import com.Quartz.QuartzJob.MatchesQuartzJob;
import com.Quartz.Timer.Timer;
import com.Quartz.service.TimerService.SchedulerService;


@Service
public class RunningSchedulerService {

	private SchedulerService schedulerService;
	
	@Autowired
	public RunningSchedulerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}
	
	@PostConstruct
	public void runChampStaticBatchJob() {
		Timer champStaticTimer = new Timer();
		champStaticTimer.setRunForever(true);
		champStaticTimer.setTotalFireCount(5);
		champStaticTimer.setCallbackData(null);
		champStaticTimer.setInitialOffsetMs(0);
		champStaticTimer.setRepeatIntervalMs(1000*60*60*24);
		
		schedulerService.schedule(ChampStaticQuartzJob.class, champStaticTimer);
	}
	
	@PostConstruct
	public void runMatchesBatchJob() {
		Timer matchesTimer = new Timer();
		matchesTimer.setRunForever(true);
		matchesTimer.setTotalFireCount(0);
		matchesTimer.setCallbackData(null);
		matchesTimer.setInitialOffsetMs(0);
		matchesTimer.setRepeatIntervalMs(2000*60);
		
		schedulerService.schedule(MatchesQuartzJob.class, matchesTimer);
	}
}
