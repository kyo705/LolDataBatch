package com.SpringBatch.repository.championrepository;

import java.util.List;

import com.SpringBatch.Entity.Champion.Champion;
import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemy;
import com.SpringBatch.Entity.Champion.ChampionItem.ChampItem;


public interface ChampionReository {

	List<Champion> findChamps(String position);

	List<ChampItem> findChampItems(String champion);

	List<ChampEnemy> findChampEnemys(String champion);

	//�����Ͻ� ������Ʈ���� ���� �κ� => �׽�Ʈ�� db ������ Ŭ�����ϱ� ���� �ڵ�
	void deleteChampsAll();
	
	void deleteChampItemsAll();
	
	void deleteChampEnemysAll();
}
