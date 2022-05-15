package com.SpringBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemy;
import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemyCompKey;

public interface ChampEnemyRepository extends JpaRepository<ChampEnemy, ChampEnemyCompKey> {

}
