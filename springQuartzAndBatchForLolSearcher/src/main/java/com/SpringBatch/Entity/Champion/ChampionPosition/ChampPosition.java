package com.SpringBatch.Entity.Champion.ChampionPosition;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class ChampPosition {
	
	@EmbeddedId
	private ChampPositionCompKey ck;
	
	private long count;

	public ChampPositionCompKey getCk() {
		return ck;
	}

	public void setCk(ChampPositionCompKey ck) {
		this.ck = ck;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	
}
