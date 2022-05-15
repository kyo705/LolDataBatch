package com.SpringBatch.Jobs.puuids;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.Quartz.QuartzJob.MatchesQuartzJob;
import com.SpringBatch.Entity.Summoner;

public class CustomMemoryPuuIdItemWriter implements ItemWriter<Summoner> {

	@Autowired
	ApplicationContext applicationContext;
	
	@Override
	public void write(List<? extends Summoner> items) throws Exception {
		MatchesQuartzJob matchesQuartzJob = applicationContext.getBean(MatchesQuartzJob.class);
		
		for(Summoner summoner : items) {
			matchesQuartzJob
			.puuIds.put(summoner.getPuuid(), summoner.getLastmatchid());
		}
		
	}

}
