package com.SpringBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringBatch.Entity.rank.Rank;
import com.SpringBatch.Entity.rank.RankCompKey;

public interface RankRepository extends JpaRepository<Rank, RankCompKey> {

}
