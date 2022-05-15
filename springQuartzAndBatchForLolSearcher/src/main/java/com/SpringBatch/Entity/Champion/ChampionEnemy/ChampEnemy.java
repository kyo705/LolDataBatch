package com.SpringBatch.Entity.Champion.ChampionEnemy;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class ChampEnemy {

	@EmbeddedId
	private ChampEnemyCompKey ck;
	
	private long wins;
	
	private long losses;

	public ChampEnemyCompKey getCk() {
		return ck;
	}

	public void setCk(ChampEnemyCompKey ck) {
		this.ck = ck;
	}

	public long getWins() {
		return wins;
	}

	public void setWins(long wins) {
		this.wins = wins;
	}

	public long getLosses() {
		return losses;
	}

	public void setLosses(long losses) {
		this.losses = losses;
	}
	
}
