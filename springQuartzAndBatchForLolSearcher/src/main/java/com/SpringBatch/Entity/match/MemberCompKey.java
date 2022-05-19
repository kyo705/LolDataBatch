package com.SpringBatch.Entity.match;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MemberCompKey implements Serializable {

	private static final long serialVersionUID = -658379631198331116L;
	@Column(name = "SUMMONER_ID")
	private String summonerid;
	@Column(name = "MATCH_ID")
	private String matchid;
	
	public MemberCompKey() {
		
	}

	public MemberCompKey(String summonerid, String matchid) {
		this.summonerid = summonerid;
		this.matchid = matchid;
	}

	public String getSummonerid() {
		return summonerid;
	}

	public void setSummonerid(String summonerid) {
		this.summonerid = summonerid;
	}

	public String getMatchid() {
		return matchid;
	}

	public void setMatchid(String matchid) {
		this.matchid = matchid;
	}

	@Override
	public int hashCode() {
		return Objects.hash(matchid, summonerid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberCompKey other = (MemberCompKey) obj;
		return Objects.equals(matchid, other.matchid) && Objects.equals(summonerid, other.summonerid);
	}
	
}
