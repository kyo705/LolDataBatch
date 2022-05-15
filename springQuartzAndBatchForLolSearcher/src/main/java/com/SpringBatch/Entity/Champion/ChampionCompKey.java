package com.SpringBatch.Entity.Champion;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ChampionCompKey implements Serializable {

	private static final long serialVersionUID = -4197653094637612827L;

	@Column(name = "CHAMPION_ID")
	private String championId;
	@Column(name = "SEASON_ID")
	private String seasonId;
	@Column(name = "POSITION")
	private String position;

	public ChampionCompKey() {}

	public ChampionCompKey(String championId, String seasonId, String position) {
		super();
		this.championId = championId;
		this.seasonId = seasonId;
		this.position = position;
	}

	public String getChampionId() {
		return championId;
	}

	public void setChampionId(String championId) {
		this.championId = championId;
	}

	public String getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public int hashCode() {
		return Objects.hash(championId, position, seasonId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChampionCompKey other = (ChampionCompKey) obj;
		return Objects.equals(championId, other.championId) && Objects.equals(position, other.position)
				&& Objects.equals(seasonId, other.seasonId);
	}
	
}
