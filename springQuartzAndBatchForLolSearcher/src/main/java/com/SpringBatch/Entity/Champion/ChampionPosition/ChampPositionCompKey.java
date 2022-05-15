package com.SpringBatch.Entity.Champion.ChampionPosition;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.SpringBatch.Entity.Champion.ChampionCompKey;

@Embeddable
public class ChampPositionCompKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private ChampionCompKey championCk;
	@Column(name = "POSITION")
	private String position;
	
	public ChampPositionCompKey() {}

	public ChampPositionCompKey(ChampionCompKey championCk, String position) {
		super();
		this.championCk = championCk;
		this.position = position;
	}

	public ChampionCompKey getChampionCk() {
		return championCk;
	}

	public void setChampionCk(ChampionCompKey championCk) {
		this.championCk = championCk;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public int hashCode() {
		return Objects.hash(championCk, position);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChampPositionCompKey other = (ChampPositionCompKey) obj;
		return Objects.equals(championCk, other.championCk) && Objects.equals(position, other.position);
	}

}
