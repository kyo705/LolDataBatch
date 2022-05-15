package com.SpringBatch.Entity.Champion;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;


@Entity
public class Champion {
	
	@EmbeddedId
	private ChampionCompKey ck;
	
	private long wins;
	
	private long losses;

	public ChampionCompKey getCk() {
		return ck;
	}

	public void setCk(ChampionCompKey ck) {
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
