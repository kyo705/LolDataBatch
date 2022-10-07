package com.SpringBatch.Jobs.championstatic;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.batch.item.database.JpaItemWriter;

import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemy;
import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemyCompKey;
import com.SpringBatch.Entity.temporarychapion.championEnemy.TemporaryChampEnemy;

public class SavingChampEnemyStaticJpaItemWriter extends JpaItemWriter<TemporaryChampEnemy> {

	@Override
	protected void doWrite(EntityManager entityManager, List<? extends TemporaryChampEnemy> items) {
		for(TemporaryChampEnemy item : items) {
			ChampEnemyCompKey champCk = new ChampEnemyCompKey(
					item.getCk().getChampionId(),
					item.getCk().getSeasonId(), 
					item.getCk().getEnemychampionId())
					;
			ChampEnemy old_champEnemy = entityManager.find(ChampEnemy.class, champCk);
			
			if(old_champEnemy==null) {
				old_champEnemy = new ChampEnemy();
				old_champEnemy.setCk(champCk);
				old_champEnemy.setWins(0);
				old_champEnemy.setLosses(0);
				
				entityManager.persist(old_champEnemy);
			}
			
			old_champEnemy.setWins(old_champEnemy.getWins()+item.getWins());
			old_champEnemy.setLosses(old_champEnemy.getLosses()+item.getLosses());
			
		}
	}
}
