package com.SpringBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringBatch.Entity.Champion.ChampionItem.ChampItem;
import com.SpringBatch.Entity.Champion.ChampionItem.ChampItemCompKey;

public interface ChampItemRepository extends JpaRepository<ChampItem, ChampItemCompKey> {

}
