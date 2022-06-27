package com.SpringBatch.Entity.match;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "MATCHES")
public class Match {

	@Id
	@Column(name = "id")
	private String matchid;
	private long gameDuration;
	private long gameEndTimestamp;
	private int queueId;
	private int season;
	
	//Member Entity의 특정 필드명, 패치전략 default값이 lazy이기 때문에 따로 설정 안함.
	//Match하나에 10개의 Member가 들어가기 때문에 ArrayList size를 default값(size : 10)으로 사용
	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.EAGER) 
	private List<Member> members = new ArrayList<>();
	
	
	public String getMatchId() {
		return matchid;
	}
	public void setMatchId(String matchId) {
		this.matchid = matchId;
	}
	public long getGameDuration() {
		return gameDuration;
	}
	public void setGameDuration(long gameDuration) {
		this.gameDuration = gameDuration;
	}
	public long getGameEndTimestamp() {
		return gameEndTimestamp;
	}
	public void setGameEndTimestamp(long gameEndTimestamp) {
		this.gameEndTimestamp = gameEndTimestamp;
	}
	public int getQueueId() {
		return queueId;
	}
	public void setQueueId(int queueId) {
		this.queueId = queueId;
	}
	public List<Member> getMembers() {
		return members;
	}
	public void setMembers(List<Member> members) {
		this.members = members;
	}
	
	public void newMember(Member member) {
		this.members.add(member);
	}
	public void removeMember(Member member) {
		this.members.remove(member);
	}
	public int getSeason() {
		return season;
	}
	public void setSeason(int season) {
		this.season = season;
	}
	
}
