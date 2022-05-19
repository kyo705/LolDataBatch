package com.SpringBatch.Entity.rank;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class RankCompKey implements Serializable {

	private static final long serialVersionUID = 5027764351283504250L;
	
	private String summonerId;
	private String queueType;
	
	public RankCompKey(){
		
	}

	public RankCompKey(String summonerId, String queueType) {
		this.summonerId = summonerId;
		this.queueType = queueType;
	}

	public String getSummonerId() {
		return summonerId;
	}

	public void setSummonerId(String summonerId) {
		this.summonerId = summonerId;
	}

	public String getQueueType() {
		return queueType;
	}

	public void setQueueType(String queueType) {
		this.queueType = queueType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(queueType, summonerId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RankCompKey other = (RankCompKey) obj;
		return Objects.equals(queueType, other.queueType) && Objects.equals(summonerId, other.summonerId);
	}
	
}
