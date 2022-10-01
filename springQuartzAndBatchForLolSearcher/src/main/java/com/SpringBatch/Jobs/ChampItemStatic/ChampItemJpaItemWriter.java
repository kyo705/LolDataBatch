package com.SpringBatch.Jobs.ChampItemStatic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.batch.item.database.JpaItemWriter;

import com.SpringBatch.Entity.Champion.ChampionItem.ChampItem;
import com.SpringBatch.Entity.Champion.ChampionItem.ChampItemCompKey;
import com.SpringBatch.Entity.match.Member;

public class ChampItemJpaItemWriter extends JpaItemWriter<Member> {

	private final int seasonId;
	
	public ChampItemJpaItemWriter(int seasonId) {
		this.seasonId = seasonId;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doWrite(EntityManager entityManager, List<? extends Member> items) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Writing to JPA with " + items.size() + " items.");
		}
		
		Map<String, Map<Integer,Long>[]> champion = new HashMap<>();
		int season = seasonId;
		
		if (!items.isEmpty()) {
			long addedToContextCount = 0;
			
			for (Member item : items) {
				
				Member member = item;
				
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
			
			Iterator<Map.Entry<String, Map<Integer,Long>[]>> iter = champion.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry<String, Map<Integer,Long>[]> entry = iter.next();
				for(int i=0;i<2;i++) {
					if(entry.getValue()[i].size() == 0) continue;
					
					Iterator<Map.Entry<Integer,Long>> iter2 = entry.getValue()[i].entrySet().iterator();
					while(iter2.hasNext()) {
						Map.Entry<Integer,Long> itemCountEntry = iter2.next();
						
						if(itemCountEntry.getKey()==0) continue;
						
						ChampItemCompKey champItemCk = new ChampItemCompKey(entry.getKey(),season,itemCountEntry.getKey());
						ChampItem champItem = entityManager.find(ChampItem.class, champItemCk);
						
						if(champItem!=null) {
							if(i==0)
								champItem.setWins(champItem.getWins()+entry.getValue()[i].get(itemCountEntry.getKey()));
							else
								champItem.setLosses(champItem.getLosses()+entry.getValue()[i].get(itemCountEntry.getKey()));
						}else {
							champItem = new ChampItem();
							champItem.setCk(champItemCk);
							if(i==0)
								champItem.setWins(entry.getValue()[i].get(itemCountEntry.getKey()));
							else
								champItem.setLosses(entry.getValue()[i].get(itemCountEntry.getKey()));
							
							entityManager.persist(champItem);
						}
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
