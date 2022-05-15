package com.SpringBatch.Entity.Champion.ChampionItem;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class ChampItem {

	@EmbeddedId
	private ChampItemCompKey ck;
	
	private long wins;
	
	private long losses;

	public ChampItemCompKey getCk() {
		return ck;
	}

	public void setCk(ChampItemCompKey ck) {
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
