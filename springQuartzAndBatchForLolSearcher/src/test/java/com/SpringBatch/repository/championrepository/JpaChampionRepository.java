package com.SpringBatch.repository.championrepository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.SpringBatch.Entity.Champion.Champion;
import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemy;
import com.SpringBatch.Entity.Champion.ChampionItem.ChampItem;


@Transactional
@Repository
public class JpaChampionRepository implements ChampionReository {
	
	private static final int seasonId = 22;
	private final EntityManager em;
	
	public JpaChampionRepository(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public List<Champion> findChamps(String position) {
		String jpql = "SELECT c FROM Champion c "
				+ "WHERE c.ck.position = :position AND c.ck.seasonId = :seasonId "
				+ "ORDER BY c.wins + c.losses DESC";
		
		return em.createQuery(jpql, Champion.class)
				.setParameter("position", position)
				.setParameter("seasonId", seasonId)
				.getResultList();
	}

	@Override
	public List<ChampItem> findChampItems(String champion) {
		String jpql = "SELECT c FROM ChampItem c "
				+ "WHERE c.ck.championId = :championId AND c.ck.seasonId = :seasonId "
				+ "ORDER BY c.wins + c.losses DESC";
		
		return em.createQuery(jpql, ChampItem.class)
				.setParameter("championId", champion)
				.setParameter("seasonId", seasonId)
				.getResultList();
	}

	@Override
	public List<ChampEnemy> findChampEnemys(String champion) {
		String jpql = "SELECT c FROM ChampEnemy c "
				+ "WHERE c.ck.championId = :championId AND c.ck.seasonId = :seasonId "
				+ "ORDER BY c.wins + c.losses DESC";
		
		return em.createQuery(jpql, ChampEnemy.class)
				.setParameter("championId", champion)
				.setParameter("seasonId", seasonId)
				.getResultList();
	}

	@Override
	public void deleteChampsAll() {
		em.createQuery("DELETE FROM Champion c").executeUpdate();
		
	}

	@Override
	public void deleteChampItemsAll() {
		em.createQuery("DELETE FROM ChampItem c").executeUpdate();
	}

	@Override
	public void deleteChampEnemysAll() {
		em.createQuery("DELETE FROM ChampEnemy c").executeUpdate();
	}

}
