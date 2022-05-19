package com.SpringBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringBatch.Entity.Summoner;

public interface SummonerRepository extends JpaRepository<Summoner, String> {

}
