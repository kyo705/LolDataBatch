package com.SpringBatch.Jobs.RankMatch;

import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;

public class RestApiCompletionPolicy implements CompletionPolicy {

	private int chunksize;
	private int totalProcessed;
	
	@Override
	public boolean isComplete(RepeatContext context, RepeatStatus result) {
		if(result == RepeatStatus.FINISHED) {
			return true;
		}else {
			return isComplete(context);
		}
	}

	@Override
	public boolean isComplete(RepeatContext context) {
		return chunksize <= totalProcessed;
	}

	@Override
	public RepeatContext start(RepeatContext parent) {
		chunksize = 30;
		totalProcessed = 0;
		return parent;
	}

	@Override
	public void update(RepeatContext context) {
		totalProcessed++;
	}

}
