package com.SpringBatch.Entity.temporarychapion;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;


@Entity
public class TemporaryChampion {
	
	@EmbeddedId
	private TemporaryChampionCompKey ck;
	
	private long wins;
	
	private long losses;

	public TemporaryChampionCompKey getCk() {
		return ck;
	}

	public void setCk(TemporaryChampionCompKey ck) {
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
