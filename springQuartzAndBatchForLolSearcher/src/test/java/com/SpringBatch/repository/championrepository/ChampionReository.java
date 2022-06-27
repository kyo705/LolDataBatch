package com.SpringBatch.repository.championrepository;

import java.util.List;

import com.SpringBatch.Entity.Champion.Champion;
import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemy;
import com.SpringBatch.Entity.Champion.ChampionItem.ChampItem;


public interface ChampionReository {

	List<Champion> findChamps(String position);

	List<ChampItem> findChampItems(String champion);

	List<ChampEnemy> findChampEnemys(String champion);

	//비지니스 프로젝트에는 없는 부분 => 테스트시 db 데이터 클리어하기 위한 코드
	void deleteChampsAll();
	
	void deleteChampItemsAll();
	
	void deleteChampEnemysAll();
}
