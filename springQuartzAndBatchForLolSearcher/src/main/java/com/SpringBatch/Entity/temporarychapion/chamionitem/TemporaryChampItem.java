package com.SpringBatch.Entity.temporarychapion.chamionitem;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class TemporaryChampItem {

	@EmbeddedId
	private TemporaryChampItemCompKey ck;
	
	private long wins;
	
	private long losses;

	public TemporaryChampItemCompKey getCk() {
		return ck;
	}

	public void setCk(TemporaryChampItemCompKey ck) {
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
