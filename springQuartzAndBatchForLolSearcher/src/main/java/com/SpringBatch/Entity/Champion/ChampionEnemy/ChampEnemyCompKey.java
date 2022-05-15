package com.SpringBatch.Entity.Champion.ChampionEnemy;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ChampEnemyCompKey implements Serializable {
	
	private static final long serialVersionUID = -5366055224920056946L;

	@Column(name = "CHAMPION_ID")
	private String championId;
	@Column(name = "SEASON_ID")
	private int seasonId;
	@Column(name = "ENEMY_CHAMPION_ID")
	private String enemychampionId;
	
	public ChampEnemyCompKey() {}

	public ChampEnemyCompKey(String championId, int seasonId, String enemychampionId) {
		super();
		this.championId = championId;
		this.seasonId = seasonId;
		this.enemychampionId = enemychampionId;
	}

	public String getChampionId() {
		return championId;
	}

	public void setChampionId(String championId) {
		this.championId = championId;
	}

	public int getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(int seasonId) {
		this.seasonId = seasonId;
	}

	public String getEnemychampionId() {
		return enemychampionId;
	}

	public void setEnemychampionId(String enemychampionId) {
		this.enemychampionId = enemychampionId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(championId, enemychampionId, seasonId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChampEnemyCompKey other = (ChampEnemyCompKey) obj;
		return Objects.equals(championId, other.championId) && Objects.equals(enemychampionId, other.enemychampionId)
				&& seasonId == other.seasonId;
	}

}
