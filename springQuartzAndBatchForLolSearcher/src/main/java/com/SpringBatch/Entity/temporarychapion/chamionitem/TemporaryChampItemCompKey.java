package com.SpringBatch.Entity.temporarychapion.chamionitem;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TemporaryChampItemCompKey implements Serializable{

	private static final long serialVersionUID = 1649475722631843255L;
	
	@Column(name = "CHAMPION_ID")
	private String championId;
	@Column(name = "SEASON_ID")
	private int seasonId;
	@Column(name = "ITEM_ID")
	private int itemId;
	
	public TemporaryChampItemCompKey() {}

	public TemporaryChampItemCompKey(String championId, int seasonId, int itemId) {
		super();
		this.championId = championId;
		this.seasonId = seasonId;
		this.itemId = itemId;
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

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(championId, itemId, seasonId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemporaryChampItemCompKey other = (TemporaryChampItemCompKey) obj;
		return Objects.equals(championId, other.championId) && itemId == other.itemId && seasonId == other.seasonId;
	}
	
}
