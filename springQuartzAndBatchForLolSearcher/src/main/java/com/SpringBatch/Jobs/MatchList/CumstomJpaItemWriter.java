package com.SpringBatch.Jobs.MatchList;

import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.batch.item.database.JpaItemWriter;
import com.SpringBatch.Entity.Summoner;


public class CumstomJpaItemWriter extends JpaItemWriter<String> {

	private String puuId;
	
	public CumstomJpaItemWriter(String summonerId) {
		this.puuId = summonerId;
	}
	
	@Override
	protected void doWrite(EntityManager entityManager, List<? extends String> items) {
		
		for(String renewLastMatchId : items) {
			
			String jpql = "SELECT s FROM Summoner s WHERE s.puuid = :puuid";
			
			Summoner summoner = entityManager
					.createQuery(jpql, Summoner.class)
					.setParameter("puuid", puuId)
					.getSingleResult();
			
			summoner.setLastmatchid(renewLastMatchId);
		}
	}

}
