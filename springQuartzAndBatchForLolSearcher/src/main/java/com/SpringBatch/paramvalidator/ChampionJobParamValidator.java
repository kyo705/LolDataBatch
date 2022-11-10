package com.SpringBatch.paramvalidator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class ChampionJobParamValidator implements JobParametersValidator {

	private static final int soloRankId = 420;
	private static final int teamRankId = 440;
	private static final int currentSeasonId = 12;
	
	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		try {
			long curTimeStamp = parameters.getLong("currentTimeStamp");
			int queueId = Long.valueOf(parameters.getLong("queueId")).intValue();
			int seasonId = Long.valueOf(parameters.getLong("seasonId")).intValue();
			
			if(queueId!=soloRankId&&queueId!=teamRankId) {
				throw new JobParametersInvalidException("QueueId is not invalid");
			}
			
			if(seasonId!=currentSeasonId) {
				throw new JobParametersInvalidException("SeasonId is not currentSeasonId");
			}
		}catch(NullPointerException e) {
			throw new JobParametersInvalidException("One of parameters is null");
		}catch(ClassCastException e) {
			throw new JobParametersInvalidException("One of parameters does not match type");
		}
	}

}
