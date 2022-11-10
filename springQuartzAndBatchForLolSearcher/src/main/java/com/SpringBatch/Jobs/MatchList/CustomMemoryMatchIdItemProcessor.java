package com.SpringBatch.Jobs.MatchList;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.quartz.job.MatchesQuartzJob;

public class CustomMemoryMatchIdItemProcessor implements ItemProcessor<List<String>, String> {

	@Autowired
	private ApplicationContext applicationContext;
	
	private String lastMatchId;
	
	public CustomMemoryMatchIdItemProcessor(String lastMatchId) {
		this.lastMatchId = lastMatchId;
	}

	@Override
	public String process(List<String> item) throws Exception {
		String renewLastMatchId = item.get(0);
		Iterator<String> iter = item.iterator();
		while(iter.hasNext()) {
			String matchid = iter.next();
			
			if(matchid.equals(lastMatchId)) {
				break;
			}
			
			MatchesQuartzJob matchesQuartzJob = applicationContext.getBean(MatchesQuartzJob.class);
			
			matchesQuartzJob.matchIds.put(matchid, matchid);
		}
		return renewLastMatchId;
	}

}
