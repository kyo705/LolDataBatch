package com.SpringBatch.Jobs.champion;

import java.util.Arrays;
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

public class ChampionJpaItemWriter extends JpaItemWriter<Match> {
	
	boolean invalid = false;
	
	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		if(invalid) {
			return ExitStatus.STOPPED;
		}else {
			return stepExecution.getExitStatus();
		}
	}
	
	@Override
	protected void doWrite(EntityManager entityManager, List<? extends Match> items) {
		Map<String, Map<String,Long>[]> championPositionStatistic = new HashMap<>();
		Map<String, Map<String,Long>[]> championEnmeyStatistic = new HashMap<>();
		Map<String, Map<Integer,Long>[]> championItemStatistic = new HashMap<>();
		
		int seasonId = 0;
		
		if(items.isEmpty()) {
			return;
		}
		for (Match match : items) {
			if(match.getQueueId()!=420) {
				invalid = true;
				return;
			}
			seasonId = match.getSeason();
			
			for(Member member : match.getMembers()) {
				String championId = member.getChampionid();
				boolean isWin = member.getWins();
				String position = member.getPositions();
				List<Integer> gameItems = Arrays.asList(
						member.getItem0(), member.getItem1(), member.getItem2(),
						member.getItem3(),member.getItem4(),member.getItem5());
				
				if(position==null) {
					invalid = true;
					return;
				}
				//특정 챔피언 포지션별 이긴 횟수, 진 횟수 체크
				countChampionPosition(championId, position, isWin, championPositionStatistic);
				//특정 챔피언이 선택한 아이템별 이긴 횟수, 진 횟수 체크
				countChampionItem(championId, gameItems, isWin, championItemStatistic);
				//특정 챔피언의 상대 챔피언별 이긴 횟수, 진 횟수 체크
				String enemyChampId = findSamePositionEnemy(championId, position, match.getMembers());
				countChampionEnemy(championId, enemyChampId, position, isWin, championEnmeyStatistic);
			}
		}
		renewChampionsPosition(seasonId, entityManager, championPositionStatistic);
		renewChampionsItem(seasonId, entityManager, championItemStatistic);
		renewChampionEnemy(seasonId, entityManager, championEnmeyStatistic);
	}
	

	@SuppressWarnings("unchecked")
	private void countChampionPosition(String champId, String position, 
			boolean isWin, Map<String, Map<String, Long>[]> champStatic) {
		Map<String, Long> positionCount = null;
		
		if(!champStatic.containsKey(champId)){
			champStatic.put(champId, new HashMap[] {new HashMap<>(), new HashMap<>()});
		}
		
		if(isWin) {
			positionCount = champStatic.get(champId)[0];
		}else {
			positionCount = champStatic.get(champId)[1];
		}
		positionCount.put(position, positionCount.getOrDefault(position, 0l)+1);
	}
	
	
	@SuppressWarnings("unchecked")
	private void countChampionItem(String champId, List<Integer> gameItems, 
			boolean isWin, Map<String, Map<Integer, Long>[]> champItemStatic) {
		Map<Integer,Long> itemCount = null;
		
		if(!champItemStatic.containsKey(champId)) {
			champItemStatic.put(champId, new HashMap[] {new HashMap<>(), new HashMap<>()});
		}
		
		if(isWin) {
			itemCount = champItemStatic.get(champId)[0];
		}else {
			itemCount = champItemStatic.get(champId)[1];
		}
		//item이 0인 경우는 아이템이 없는 경우이므로 값을 넣어주지 않음
		for(Integer gameItem : gameItems) {
			if(gameItem == 0) {
				continue;
			}
			itemCount.put(gameItem, itemCount.getOrDefault(gameItem, 0L)+1);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void countChampionEnemy(String champId, String enemyChampId, String position, 
			boolean isWin, Map<String, Map<String, Long>[]> champEnmeyStatic) {
		Map<String,Long> enemyCount = null;
		
		if(!champEnmeyStatic.containsKey(champId))  {
			champEnmeyStatic.put(champId, new Map[] {new HashMap<String,Long>(),new HashMap<String,Long>()});
		}
		
		if(isWin) {
			enemyCount = champEnmeyStatic.get(champId)[0];
		}else {
			enemyCount =  champEnmeyStatic.get(champId)[1];
		}
		enemyCount.put(enemyChampId, enemyCount.getOrDefault(enemyChampId, 0L)+1);
	}
	
	
	private String findSamePositionEnemy(String champId, String position, List<Member> members) {
		for(Member member : members) {
			String memberChampId = member.getChampionid();
			String memberPosition = member.getPositions();
			
			if(memberChampId.equals(champId)) {
				continue;
			}
			if(!memberPosition.equals(position)) {
				continue;
			}
			return memberChampId;
		}
		//예외상황 
		return null;
	}
	
	
	private void renewChampionsPosition(int seasonId, EntityManager entityManager, 
			Map<String, Map<String, Long>[]> championPositionStatistic) {
		for(Map.Entry<String, Map<String, Long>[]> entry1 : championPositionStatistic.entrySet()) {
			String championId = entry1.getKey();
			Map<String, Long>[] championPositionsCount = entry1.getValue();
			
			for(int i=0;i<2;i++) {
				if(championPositionsCount[i].size()==0) {
					continue;
				}
				for(Map.Entry<String, Long> entry2 : championPositionsCount[i].entrySet()) {
					String position = entry2.getKey();
					Long count = entry2.getValue();
					
					TemporaryChampionCompKey champCk = new TemporaryChampionCompKey(championId, seasonId, position);
					TemporaryChampion champion = entityManager.find(TemporaryChampion.class, champCk);
					if(champion==null) {
						champion = new TemporaryChampion();
						champion.setCk(champCk);
						champion.setWins(0l);
						champion.setLosses(0l);
						
						entityManager.persist(champion);
					}
					
					if(i==0)
						champion.setWins(champion.getWins()+count);
					else
						champion.setLosses(champion.getLosses()+count);
				}
			}
		}
	}


	private void renewChampionsItem(int seasonId, EntityManager entityManager,
			Map<String, Map<Integer, Long>[]> championItemStatistic) {
		for(Map.Entry<String, Map<Integer,Long>[]> entry1 : championItemStatistic.entrySet()) {
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
	}
	
	
	private void renewChampionEnemy(int seasonId, EntityManager entityManager,
			Map<String, Map<String, Long>[]> championEnmeyStatistic) {
		for(Map.Entry<String, Map<String, Long>[]> entry1 : championEnmeyStatistic.entrySet()) {
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
