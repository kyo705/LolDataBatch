package com.SpringBatch.Jobs.ChampStatic;


import java.util.List;


import javax.persistence.EntityManager;

import org.springframework.batch.item.database.JpaItemWriter;

import com.SpringBatch.Entity.Champion.Champion;

public class ChampStaticItemWriter extends JpaItemWriter<Champion> {
	
	@Override
	protected void doWrite(EntityManager entityManager, List<? extends Champion> items) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Writing to JPA with " + items.size() + " items.");
		}
		
		if (!items.isEmpty()) {
			long addedToContextCount = 0;
			for (Champion item : items) {
				if (!entityManager.contains(item)) {
					Champion existChampion = entityManager.find(Champion.class, item.getCk());
					
					if(existChampion!=null) {
						long existWins = existChampion.getWins();
						long existLosses = existChampion.getLosses();
						
						existChampion.setWins(existWins + item.getWins());
						existChampion.setLosses(existLosses + item.getLosses());
						
					}else { //DB에 일치하는 Entity가 없을 때(신규 Entity)
						entityManager.persist(item);
						addedToContextCount++;
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug(addedToContextCount + " entities " + "persisted.");
				logger.debug((items.size() - addedToContextCount) + " entities found in persistence context.");
			}
		}
	}
}
