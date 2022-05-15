package com.SpringBatch.Jobs.ChampEnemyStatic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.batch.item.database.JpaItemWriter;

import com.SpringBatch.Entity.Match;
import com.SpringBatch.Entity.Member;
import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemy;
import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemyCompKey;

public class champEnemyJpaItemWriter extends JpaItemWriter<Match> {
	
	@Override
	protected void doWrite(EntityManager entityManager, List<? extends Match> items) {
		
		Map<String, Map<String,Long>[]> championEnmey = new HashMap<>();
		int season = 0;
		
		if (!items.isEmpty()) {
			boolean plag = true;
			
			for (Match item : items) {
				if(plag) {
					season = item.getSeason();
					plag = false;
				}
				
				List<Member> members = item.getMembers();		
				Iterator<Member> memberIter = members.iterator();
				while(memberIter.hasNext()) {
					Member member = memberIter.next();
					String champId = member.getChampionid();
					String position = member.getPositions();
					
					Iterator<Member> enemyMemberIter = members.iterator();
					while(enemyMemberIter.hasNext()) {
						Member enemy = enemyMemberIter.next();
						String enemyChampId = enemy.getChampionid();
						String enemyPosition = enemy.getPositions();
						if(!champId.equals(enemyChampId) 
								&& position.equals(enemyPosition)) {
							
							if(member.getWins()) {
								Map<String,Long> champWinEnemy = championEnmey.get(champId)[0];
								champWinEnemy.put(enemyChampId, champWinEnemy.getOrDefault(enemyChampId, 0L)+1);
							}else {
								Map<String,Long> champLossEnemy = championEnmey.get(champId)[1];
								champLossEnemy.put(enemyChampId, champLossEnemy.getOrDefault(enemyChampId, 0L)+1);
							}
							
							break;
						}
					}
				}
			}
			
			//db에서 enemy 테이블 조회하고 값 변경
			Iterator<Map.Entry<String, Map<String,Long>[]>> iter = championEnmey.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<String, Map<String,Long>[]> entry = iter.next();
				
				for(int i=0;i<2;i++) {
					Iterator<Map.Entry<String,Long>> enemyCountIter = entry.getValue()[i].entrySet().iterator();
					Map.Entry<String,Long> EnemyCountEntry = enemyCountIter.next();
					ChampEnemyCompKey champEnemyCk = new ChampEnemyCompKey(entry.getKey(), season, EnemyCountEntry.getKey());
					ChampEnemy champEnemy = entityManager.find(ChampEnemy.class, champEnemyCk);
					
					if(champEnemy!=null) {
						if(i==0)
							champEnemy.setWins(champEnemy.getWins()+EnemyCountEntry.getValue());
						else 
							champEnemy.setLosses(champEnemy.getLosses()+EnemyCountEntry.getValue());
						
					}else {
						champEnemy = new ChampEnemy();
						champEnemy.setCk(champEnemyCk);
						if(i==0) {
							champEnemy.setWins(EnemyCountEntry.getValue());
							champEnemy.setLosses(0L);
						}else {
							champEnemy.setWins(0L);
							champEnemy.setLosses(EnemyCountEntry.getValue());
						}
						
						entityManager.persist(champEnemy);
					}
					
				}
				
			}
		}
	}
}
