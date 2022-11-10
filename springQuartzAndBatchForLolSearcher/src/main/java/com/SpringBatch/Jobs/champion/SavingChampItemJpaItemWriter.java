package com.SpringBatch.Jobs.champion;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.batch.item.database.JpaItemWriter;

import com.SpringBatch.Entity.Champion.ChampionItem.ChampItem;
import com.SpringBatch.Entity.Champion.ChampionItem.ChampItemCompKey;
import com.SpringBatch.Entity.temporarychapion.chamionitem.TemporaryChampItem;

public class SavingChampItemJpaItemWriter extends JpaItemWriter<TemporaryChampItem> {

	@Override
	protected void doWrite(EntityManager entityManager, List<? extends TemporaryChampItem> items) {
		for(TemporaryChampItem item : items) {
			ChampItemCompKey champCk = new ChampItemCompKey(
					item.getCk().getChampionId(),
					item.getCk().getSeasonId(), 
					item.getCk().getItemId());
			ChampItem old_champItem = entityManager.find(ChampItem.class, champCk);
			
			if(old_champItem==null) {
				old_champItem = new ChampItem();
				old_champItem.setCk(champCk);
				old_champItem.setWins(0);
				old_champItem.setLosses(0);
				
				entityManager.persist(old_champItem);
			}
			
			old_champItem.setWins(old_champItem.getWins()+item.getWins());
			old_champItem.setLosses(old_champItem.getLosses()+item.getLosses());
			
		}
	}
}
