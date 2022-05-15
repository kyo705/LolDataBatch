package com.SpringBatch.Jobs.ChampItemStatic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.batch.item.database.JpaItemWriter;

import com.SpringBatch.Entity.Match;
import com.SpringBatch.Entity.Member;
import com.SpringBatch.Entity.Champion.ChampionItem.ChampItem;
import com.SpringBatch.Entity.Champion.ChampionItem.ChampItemCompKey;

public class ChampItemJpaItemWriter extends JpaItemWriter<Match> {

	@SuppressWarnings("unchecked")
	@Override
	protected void doWrite(EntityManager entityManager, List<? extends Match> items) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Writing to JPA with " + items.size() + " items.");
		}
		
		Map<String, Map<Integer,Long>[]> champion = new HashMap<>();
		int season = 0;
		
		if (!items.isEmpty()) {
			long addedToContextCount = 0;
			boolean plag = true;
			
			for (Match item : items) {
				List<Member> members = item.getMembers();
				
				Iterator<Member> iter = members.iterator();
				
				if(plag) {
					season = item.getSeason();
					plag = false;
				}
				
				while(iter.hasNext()) {
					Member member = iter.next();
					
					String champId = member.getChampionid();
					int item0 = member.getItem0();
					int item1 = member.getItem1();
					int item2 = member.getItem2();
					int item3 = member.getItem3();
					int item4 = member.getItem4();
					int item5 = member.getItem5();
					
					if(champion.get(champId)==null) {
						champion.put(champId, new Map[] {new HashMap<Integer,Long>(), new HashMap<Integer,Long>()});
					}
					
					if(member.getWins()) {
						Map<Integer,Long> champWinItems = champion.get(champId)[0];
						champWinItems.put(item0, champWinItems.getOrDefault(item0, 0L)+1);
						champWinItems.put(item1, champWinItems.getOrDefault(item1, 0L)+1);
						champWinItems.put(item2, champWinItems.getOrDefault(item2, 0L)+1);
						champWinItems.put(item3, champWinItems.getOrDefault(item3, 0L)+1);
						champWinItems.put(item4, champWinItems.getOrDefault(item4, 0L)+1);
						champWinItems.put(item5, champWinItems.getOrDefault(item5, 0L)+1);
					}else {
						Map<Integer,Long> champLossItems = champion.get(champId)[1];
						champLossItems.put(item0, champLossItems.getOrDefault(item0, 0L)+1);
						champLossItems.put(item1, champLossItems.getOrDefault(item1, 0L)+1);
						champLossItems.put(item2, champLossItems.getOrDefault(item2, 0L)+1);
						champLossItems.put(item3, champLossItems.getOrDefault(item3, 0L)+1);
						champLossItems.put(item4, champLossItems.getOrDefault(item4, 0L)+1);
						champLossItems.put(item5, champLossItems.getOrDefault(item5, 0L)+1);
					}
				}
			}
			
			Iterator<Map.Entry<String, Map<Integer,Long>[]>> iter = champion.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<String, Map<Integer,Long>[]> entry = iter.next();
				
				Iterator<Map.Entry<Integer,Long>> iter2 = entry.getValue()[0].entrySet().iterator();
				while(iter2.hasNext()) {
					Map.Entry<Integer,Long> itemCountEntry = iter2.next();
					
					ChampItemCompKey champItemCk = new ChampItemCompKey(entry.getKey(),season,itemCountEntry.getKey());
					ChampItem champItem = entityManager.find(ChampItem.class, champItemCk);
					
					if(champItem!=null) {
						champItem.setWins(champItem.getWins()+entry.getValue()[0].get(itemCountEntry.getKey()));
						champItem.setLosses(champItem.getLosses()+entry.getValue()[1].get(itemCountEntry.getKey()));
					}else {
						champItem = new ChampItem();
						champItem.setCk(champItemCk);
						champItem.setWins(entry.getValue()[0].get(itemCountEntry.getKey()));
						champItem.setLosses(entry.getValue()[1].get(itemCountEntry.getKey()));
						
						entityManager.persist(champItem);
					}
				}
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug(addedToContextCount + " entities " + "merged.");
				logger.debug((items.size() - addedToContextCount) + " entities found in persistence context.");
			}
		}
	}
}
