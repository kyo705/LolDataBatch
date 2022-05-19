package com.SpringBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringBatch.Entity.Champion.Champion;
import com.SpringBatch.Entity.Champion.ChampionCompKey;

public interface ChampionRepository extends JpaRepository<Champion, ChampionCompKey> {

}
