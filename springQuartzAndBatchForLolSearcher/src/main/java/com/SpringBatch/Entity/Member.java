package com.SpringBatch.Entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "MEMBERS")
public class Member {
	
	@EmbeddedId
	private MemberCompKey ck;
	private String puuid;
	private String name;
	private String championid;
	private String positions;
	private boolean wins;
	
	private int team;
	private int champLevel;
	private int cs;
	private int gold;
	private int bountylevel;
	
	private int kills;
	private int deaths;
	private int assists;
	
	private int visionscore;
	private int detectorwardplaced;
	private int wardkill;
	private int wardpalced;
	private int visionWardsBoughtInGame;
	
	private int baronkills;
	private int dragonkills;
	private int inhibitorkills; //억제기 제거 횟수
	private int nexuskills;     //넥서스 제거 횟수
	
	private int doublekills;
	private int triplekills;
	private int quadrakills;
	private int pentakills;
	
	private int item0;
	private int item1;
	private int item2;
	private int item3;
	private int item4;
	private int item5;
	private int item6;
	
	@MapsId("matchid") //MemberCompKey.matchid 필드 매핑
	@ManyToOne(fetch = FetchType.EAGER) //lazy 패치로 두는 이유는 mostchamp에서 Member 테이블만 사용하기 때문
	@JoinColumn(name = "MATCH_ID")
	private Match match;
	
	public MemberCompKey getCk() {
		return ck;
	}

	public void setCk(MemberCompKey ck) {
		this.ck = ck;
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

	public String getChampionid() {
		return championid;
	}

	public void setChampionid(String championid) {
		this.championid = championid;
	}

	public int getChampLevel() {
		return champLevel;
	}

	public void setChampLevel(int champLevel) {
		this.champLevel = champLevel;
	}

	public int getCs() {
		return cs;
	}

	public void setCs(int cs) {
		this.cs = cs;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatch(Match match) {
		if(this.match!=null) {
			this.match.removeMember(this);
		}
		this.match = match;
		this.match.newMember(this);
	}
	
	public int getVisionscore() {
		return visionscore;
	}

	public void setVisionscore(int visionscore) {
		this.visionscore = visionscore;
	}

	public int getDetectorwardplaced() {
		return detectorwardplaced;
	}

	public void setDetectorwardplaced(int detectorwardplaced) {
		this.detectorwardplaced = detectorwardplaced;
	}

	public int getWardkill() {
		return wardkill;
	}

	public void setWardkill(int wardkill) {
		this.wardkill = wardkill;
	}

	public int getWardpalced() {
		return wardpalced;
	}

	public void setWardpalced(int wardpalced) {
		this.wardpalced = wardpalced;
	}

	
	public int getBaronkills() {
		return baronkills;
	}

	public void setBaronkills(int baronkills) {
		this.baronkills = baronkills;
	}

	public int getDragonkills() {
		return dragonkills;
	}

	public void setDragonkills(int dragonkills) {
		this.dragonkills = dragonkills;
	}

	public int getInhibitorkills() {
		return inhibitorkills;
	}

	public void setInhibitorkills(int inhibitorkills) {
		this.inhibitorkills = inhibitorkills;
	}

	public int getNexuskills() {
		return nexuskills;
	}

	public void setNexuskills(int nexuskills) {
		this.nexuskills = nexuskills;
	}

	public int getDoublekills() {
		return doublekills;
	}

	public void setDoublekills(int doublekills) {
		this.doublekills = doublekills;
	}

	public int getTriplekills() {
		return triplekills;
	}

	public void setTriplekills(int triplekills) {
		this.triplekills = triplekills;
	}

	public int getQuadrakills() {
		return quadrakills;
	}

	public void setQuadrakills(int quadrakills) {
		this.quadrakills = quadrakills;
	}

	public int getPentakills() {
		return pentakills;
	}

	public void setPentakills(int pentakills) {
		this.pentakills = pentakills;
	}

	public int getItem0() {
		return item0;
	}

	public void setItem0(int item0) {
		this.item0 = item0;
	}

	public int getItem1() {
		return item1;
	}

	public void setItem1(int item1) {
		this.item1 = item1;
	}

	public int getItem2() {
		return item2;
	}

	public void setItem2(int item2) {
		this.item2 = item2;
	}

	public int getItem3() {
		return item3;
	}

	public void setItem3(int item3) {
		this.item3 = item3;
	}

	public int getItem4() {
		return item4;
	}

	public void setItem4(int item4) {
		this.item4 = item4;
	}

	public int getItem5() {
		return item5;
	}

	public void setItem5(int item5) {
		this.item5 = item5;
	}

	public int getItem6() {
		return item6;
	}

	public void setItem6(int item6) {
		this.item6 = item6;
	}

	public String getPositions() {
		return positions;
	}

	public void setPositions(String positions) {
		this.positions = positions;
	}

	public boolean getWins() {
		return wins;
	}

	public void setWins(boolean wins) {
		this.wins = wins;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getBountylevel() {
		return bountylevel;
	}

	public void setBountylevel(int bountylevel) {
		this.bountylevel = bountylevel;
	}

	public int getVisionWardsBoughtInGame() {
		return visionWardsBoughtInGame;
	}

	public void setVisionWardsBoughtInGame(int visionWardsBoughtInGame) {
		this.visionWardsBoughtInGame = visionWardsBoughtInGame;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	
	/*
	 * public void newMatch(Match match) { if(this.match!=null) {
	 * this.match.removeMember(this); } this.match = match;
	 * this.match.newMember(this); }
	 */
}
