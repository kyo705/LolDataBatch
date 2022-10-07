package com.SpringBatch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;

public class ChampStaticStepExecutionListener implements StepExecutionListener{

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext context = stepExecution.getExecutionContext();
		
		
		context.put("init_value", context);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		//데이터 무결성 검사
		ExecutionContext context = stepExecution.getExecutionContext();
		
		if(context.containsKey("invalid_condition")) {
			return ExitStatus.STOPPED;
		}else {
			return ExitStatus.COMPLETED;
		}
		
	}

}
