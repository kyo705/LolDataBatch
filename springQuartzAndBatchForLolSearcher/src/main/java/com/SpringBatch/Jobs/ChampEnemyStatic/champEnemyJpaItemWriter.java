package com.SpringBatch.Jobs.ChampEnemyStatic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.batch.item.database.JpaItemWriter;

import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemy;
import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemyCompKey;
import com.SpringBatch.Entity.match.Match;
import com.SpringBatch.Entity.match.Member;

public class champEnemyJpaItemWriter extends JpaItemWriter<Match> {
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doWrite(EntityManager entityManager, List<? extends Match> items) {
		
		Map<String, Map<String,Long>[]> championEnmey = new HashMap<>();
		//시즌 바뀔때마다 바꿔줘야하는 값
		int seasonId = 22;
		
		if (!items.isEmpty()) {
			for (Match item : items) {
				for(Member member : item.getMembers()) {
					String champId = member.getChampionid();
					String position = member.getPositions();
					for(Member enemy_member : item.getMembers()) {
						String enemyChampId = enemy_member.getChampionid();
						String enemyPosition = enemy_member.getPositions();
						
						if(!champId.equals(enemyChampId) && position.equals(enemyPosition)) {
							if(member.getWins()) {
								Map<String,Long> champWinEnemy = null;
								
								if(!championEnmey.containsKey(champId))  {
									championEnmey.put(champId, new Map[] {new HashMap<String,Long>(),new HashMap<String,Long>()});
								}
								champWinEnemy = championEnmey.get(champId)[0];
								champWinEnemy.put(enemyChampId, champWinEnemy.getOrDefault(enemyChampId, 0L)+1);
							}else {
								Map<String,Long> champLossEnemy = null;
								
								if(!championEnmey.containsKey(champId)) {
									championEnmey.put(champId, new Map[] {new HashMap<String,Long>(),new HashMap<String,Long>()});
								}
								champLossEnemy = championEnmey.get(champId)[1];
								champLossEnemy.put(enemyChampId, champLossEnemy.getOrDefault(enemyChampId, 0L)+1);
							}
						}
					}
				}
			}
			
			//db에서 enemy 테이블 조회하고 값 변경
			for(Map.Entry<String, Map<String, Long>[]> entry1 : championEnmey.entrySet()) {
				String champId = entry1.getKey();
				Map<String,Long>[] champ_enemy_info = entry1.getValue();
				
				for(int i=0;i<2;i++) {
					if(champ_enemy_info[i].size()==0) {
						continue;
					}
					
					for(Map.Entry<String, Long> entry2 : champ_enemy_info[i].entrySet()) {
						String enemy_champId = entry2.getKey();
						long count = entry2.getValue();
						
						ChampEnemyCompKey champEnemyCk = new ChampEnemyCompKey(champId, seasonId, enemy_champId);
						ChampEnemy champEnemy = entityManager.find(ChampEnemy.class, champEnemyCk);
						
						if(champEnemy==null) {
							champEnemy = new ChampEnemy();
							champEnemy.setCk(champEnemyCk);
							champEnemy.setWins(0l);
							champEnemy.setLosses(0l);
							entityManager.persist(champEnemy);
						}
						
						if(i==0)
							champEnemy.setWins(champEnemy.getWins()+count);
						else
							champEnemy.setLosses(champEnemy.getLosses()+count);
						
					}
				}
			}
		}
	}
}
