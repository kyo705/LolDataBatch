package com.SpringBatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SpringBatch.Entity.Match;

public interface MatchRepository extends JpaRepository<Match, String> {

}
