package com.SpringBatch.Entity.temporarychapion.championEnemy;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class TemporaryChampEnemy {

	@EmbeddedId
	private TemporaryChampEnemyCompKey ck;
	
	private long wins;
	
	private long losses;

	public TemporaryChampEnemyCompKey getCk() {
		return ck;
	}

	public void setCk(TemporaryChampEnemyCompKey ck) {
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
