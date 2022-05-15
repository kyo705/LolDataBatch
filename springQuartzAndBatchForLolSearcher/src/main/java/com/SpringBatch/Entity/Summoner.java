package com.SpringBatch.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.gson.annotations.SerializedName;

@Entity
public class Summoner {

	@Id
	@SerializedName(value = "id")
	private String id;
	@SerializedName(value = "accountId")
	private String accountId;
	@SerializedName(value = "puuid")
	private String puuid;
	@SerializedName(value = "name")
	private String name;
	@SerializedName(value = "profileIconId")
	private int profileIconId;
	@SerializedName(value = "revisionDate")
	private long revisionDate;
	@SerializedName(value = "summonerLevel")
	private long summonerLevel;
	
	private String lastmatchid;
	
	private long lastRenewTimeStamp;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPuuid() {
		return puuid;
	}
	public void setPuuid(String puuid) {
		this.puuid = puuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProfileIconId() {
		return profileIconId;
	}
	public void setProfileIconId(int profileIconId) {
		this.profileIconId = profileIconId;
	}
	public long getRevisionDate() {
		return revisionDate;
	}
	public void setRevisionDate(long revisionDate) {
		this.revisionDate = revisionDate;
	}
	public long getSummonerLevel() {
		return summonerLevel;
	}
	public void setSummonerLevel(long summonerLevel) {
		this.summonerLevel = summonerLevel;
	}
	public String getLastmatchid() {
		return lastmatchid;
	}
	public void setLastmatchid(String lastmatchid) {
		this.lastmatchid = lastmatchid;
	}
	public long getLastRenewTimeStamp() {
		return lastRenewTimeStamp;
	}
	public void setLastRenewTimeStamp(long lastRenewTimeStamp) {
		this.lastRenewTimeStamp = lastRenewTimeStamp;
	}
}
