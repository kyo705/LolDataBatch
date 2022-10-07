package com.SpringBatch.Jobs.championstatic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.database.JpaItemWriter;

import com.SpringBatch.Entity.match.Match;
import com.SpringBatch.Entity.match.Member;
import com.SpringBatch.Entity.temporarychapion.TemporaryChampion;
import com.SpringBatch.Entity.temporarychapion.TemporaryChampionCompKey;
import com.SpringBatch.Entity.temporarychapion.chamionitem.TemporaryChampItem;
import com.SpringBatch.Entity.temporarychapion.chamionitem.TemporaryChampItemCompKey;
import com.SpringBatch.Entity.temporarychapion.championEnemy.TemporaryChampEnemy;
import com.SpringBatch.Entity.temporarychapion.championEnemy.TemporaryChampEnemyCompKey;

public class ChampStaticJpaItemWriter extends JpaItemWriter<Match> {
	
	boolean invalid_condition = false;
	
	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		//데이터 무결성 검사
		if(invalid_condition) {
			//db 데이터 삭제
			return ExitStatus.STOPPED;
		}else {
			return stepExecution.getExitStatus();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doWrite(EntityManager entityManager, List<? extends Match> items) {
		Map<String, Map<String,Long>[]> champStatic = new HashMap<>();
		Map<String, Map<String,Long>[]> champEnmeyStatic = new HashMap<>();
		Map<String, Map<Integer,Long>[]> champItemStatic = new HashMap<>();
		
		int seasonId = 0;
		
		if (!items.isEmpty()) {
			for (Match item : items) {
				if(item.getQueueId()!=420) {
					invalid_condition = true;
					break;
				}
				
				seasonId = item.getSeason();
				
				for(Member member : item.getMembers()) {
					String champId = member.getChampionid();
					boolean isWin = member.getWins();
					int champItem0 = member.getItem0();
					int champItem1 = member.getItem0();
					int champItem2 = member.getItem0();
					int champItem3 = member.getItem0();
					int champItem4 = member.getItem0();
					int champItem5 = member.getItem0();
					String position = member.getPositions();
					
					if(!champStatic.containsKey(champId)){
						champStatic.put(champId, new HashMap[] {new HashMap<>(), new HashMap<>()});
					}
					
					if(!champItemStatic.containsKey(champId)) {
						champItemStatic.put(champId, new HashMap[] {new HashMap<>(), new HashMap<>()});
					}
					
					if(!champEnmeyStatic.containsKey(champId))  {
						champEnmeyStatic.put(champId, new Map[] {new HashMap<String,Long>(),new HashMap<String,Long>()});
					}
					
					//특정 챔피언의 이긴 횟수, 진 횟수 체크
					if(position!=null) {
						if(isWin) {
							Map<String, Long> champWin = champStatic.get(champId)[0];
							champWin.put(position, champWin.getOrDefault(position, 0l)+1);
						}else {
							Map<String, Long> champLoss = champStatic.get(champId)[0];
							champLoss.put(position, champLoss.getOrDefault(position, 0l)+1);
						}
					}
					
					//특정 챔피언이 선택한 아이템의 이긴 횟수, 진 횟수 체크
					if(isWin) {			
						Map<Integer,Long> champWinItems = champItemStatic.get(champId)[0];
						
						//item이 0인 경우는 아이템이 없는 경우이므로 값을 넣어주지 않음
						if(champItem0!=0)
							champWinItems.put(champItem0, champWinItems.getOrDefault(champItem0, 0L)+1);
						if(champItem1!=0)
							champWinItems.put(champItem1, champWinItems.getOrDefault(champItem1, 0L)+1);
						if(champItem2!=0)	
							champWinItems.put(champItem2, champWinItems.getOrDefault(champItem2, 0L)+1);
						if(champItem3!=0)
							champWinItems.put(champItem3, champWinItems.getOrDefault(champItem3, 0L)+1);
						if(champItem4!=0)
							champWinItems.put(champItem4, champWinItems.getOrDefault(champItem4, 0L)+1);
						if(champItem5!=0)	
							champWinItems.put(champItem5, champWinItems.getOrDefault(champItem5, 0L)+1);
					}else {
						Map<Integer,Long> champLossItems = champItemStatic.get(champId)[1];
						champLossItems.put(champItem0, champLossItems.getOrDefault(champItem0, 0L)+1);
						champLossItems.put(champItem1, champLossItems.getOrDefault(champItem1, 0L)+1);
						champLossItems.put(champItem2, champLossItems.getOrDefault(champItem2, 0L)+1);
						champLossItems.put(champItem3, champLossItems.getOrDefault(champItem3, 0L)+1);
						champLossItems.put(champItem4, champLossItems.getOrDefault(champItem4, 0L)+1);
						champLossItems.put(champItem5, champLossItems.getOrDefault(champItem5, 0L)+1);
					}
					
					//특정 챔피언의 라인별 상대 챔피언의 이긴 횟수, 진 횟수 체크
					if(position!=null) {
						for(Member enemy_member : item.getMembers()) {
							String enemyChampId = enemy_member.getChampionid();
							String enemyPosition = enemy_member.getPositions();
							
							if(!champId.equals(enemyChampId) && position.equals(enemyPosition)) {
								if(isWin) {
									Map<String,Long> champWinEnemy = champEnmeyStatic.get(champId)[0];
									champWinEnemy.put(enemyChampId, champWinEnemy.getOrDefault(enemyChampId, 0L)+1);
								}else {
									Map<String,Long> champLossEnemy =  champEnmeyStatic.get(champId)[1];
									champLossEnemy.put(enemyChampId, champLossEnemy.getOrDefault(enemyChampId, 0L)+1);
								}
							}
						}
					}
				}
			}
			
			//db에서 champion 테이블 조회하고 값 갱신
			for(Map.Entry<String, Map<String, Long>[]> entry1 : champStatic.entrySet()) {
				String champId = entry1.getKey();
				Map<String, Long>[] champ_info = entry1.getValue();
				
				for(int i=0;i<2;i++) {
					if(champ_info[i].size()==0) {
						continue;
					}
					
					for(Map.Entry<String, Long> entry2 : champ_info[i].entrySet()) {
						String position = entry2.getKey();
						Long count = entry2.getValue();
						
						TemporaryChampionCompKey champCk = new TemporaryChampionCompKey(champId, seasonId, position);
						TemporaryChampion champ = entityManager.find(TemporaryChampion.class, champCk);
						
						if(champ==null) {
							champ = new TemporaryChampion();
							champ.setCk(champCk);
							champ.setWins(0l);
							champ.setLosses(0l);
							entityManager.persist(champ);
						}
						
						if(i==0)
							champ.setWins(champ.getWins()+count);
						else
							champ.setLosses(champ.getLosses()+count);
					}
				}
			}
			
			//db에서 championitem 테이블 조회하고 값 갱신
			for(Map.Entry<String, Map<Integer,Long>[]> entry1 : champItemStatic.entrySet()) {
				String champId = entry1.getKey();
				Map<Integer,Long>[] champ_item_info = entry1.getValue();
				
				for(int i=0;i<2;i++) {
					if(champ_item_info[i].size()==0) {
						continue;
					}
					
					for(Map.Entry<Integer, Long> entry2 : champ_item_info[i].entrySet()) {
						int champItemId = entry2.getKey();
						long count = entry2.getValue();
						TemporaryChampItemCompKey champItemCk = new TemporaryChampItemCompKey(champId, seasonId, champItemId);
						
						TemporaryChampItem champItem = entityManager.find(TemporaryChampItem.class, champItemCk);
						if(champItem==null) {
							champItem = new TemporaryChampItem();
							champItem.setCk(champItemCk);
							champItem.setWins(0l);
							champItem.setLosses(0l);
							entityManager.persist(champItem);
						}
						
						if(i==0)
							champItem.setWins(champItem.getWins()+count);
						else
							champItem.setLosses(champItem.getLosses()+count);
					}
					
				}
			}
			
			//db에서 enemy 테이블 조회하고 값 갱신
			for(Map.Entry<String, Map<String, Long>[]> entry1 : champEnmeyStatic.entrySet()) {
				String champId = entry1.getKey();
				Map<String,Long>[] champ_enemy_info = entry1.getValue();
				
				for(int i=0;i<2;i++) {
					if(champ_enemy_info[i].size()==0) {
						continue;
					}
					
					for(Map.Entry<String, Long> entry2 : champ_enemy_info[i].entrySet()) {
						String enemy_champId = entry2.getKey();
						long count = entry2.getValue();
						
						TemporaryChampEnemyCompKey champEnemyCk = new TemporaryChampEnemyCompKey(champId, seasonId, enemy_champId);
						TemporaryChampEnemy champEnemy = entityManager.find(TemporaryChampEnemy.class, champEnemyCk);
						
						if(champEnemy==null) {
							champEnemy = new TemporaryChampEnemy();
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
