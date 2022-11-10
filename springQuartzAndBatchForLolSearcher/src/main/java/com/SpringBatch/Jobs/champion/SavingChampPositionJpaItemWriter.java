package com.SpringBatch.Jobs.champion;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.batch.item.database.JpaItemWriter;

import com.SpringBatch.Entity.Champion.Champion;
import com.SpringBatch.Entity.Champion.ChampionCompKey;
import com.SpringBatch.Entity.temporarychapion.TemporaryChampion;

public class SavingChampPositionJpaItemWriter extends JpaItemWriter<TemporaryChampion>{

	@Override
	protected void doWrite(EntityManager entityManager, List<? extends TemporaryChampion> items) {
		for(TemporaryChampion item : items) {
			ChampionCompKey champCk = new ChampionCompKey(
					item.getCk().getChampionId(),
					item.getCk().getSeasonId(), 
					item.getCk().getPosition());
			
			Champion old_champ = entityManager.find(Champion.class, champCk);
			
			if(old_champ==null) {
				old_champ = new Champion();
				old_champ.setCk(champCk);
				old_champ.setWins(0);
				old_champ.setLosses(0);
				
				entityManager.persist(old_champ);
			}
			
			old_champ.setWins(old_champ.getWins()+item.getWins());
			old_champ.setLosses(old_champ.getLosses()+item.getLosses());
			
		}
	}
}
