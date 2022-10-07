package com.SpringBatch.listener;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;

import com.SpringBatch.Entity.match.Match;
import com.SpringBatch.exception.InvalidConditionException;

public class ChampStaticItemWriterListener implements ItemWriteListener<Match> {

	private static long timestamp;
	private static int seasonId;
	private static int queueId;
	
	public ChampStaticItemWriterListener(Long currentTimeStamp, int qid, int sid) {
		timestamp = currentTimeStamp;
		queueId = qid;
		seasonId = sid;
	}

	@Override
	public void beforeWrite(List<? extends Match> items) {
		for(Match match : items) {
			if(match.getSeason()!=seasonId) {
				throw new InvalidConditionException();
			}else if(match.getQueueId()!=queueId) {
				throw new InvalidConditionException();
			}else if(match.getGameEndTimestamp()>timestamp||
					match.getGameEndTimestamp()<=timestamp-1000*60*60*24) {
				throw new InvalidConditionException();
				//이전 커밋된 청크 데이터들을 롤백하는 작업이 필요함
			}
		}
	}

	@Override
	public void afterWrite(List<? extends Match> items) {
		
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Match> items) {
		// TODO Auto-generated method stub
		
	}

}
