package com.SpringBatch.Jobs.RankMatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;

import com.SpringBatch.Entity.Match;
import com.SpringBatch.Entity.Member;
import com.SpringBatch.Entity.MemberCompKey;

@SuppressWarnings({"rawtypes","unchecked"})
public class JsonToEntityItemProcessor implements ItemProcessor<Map, Match> {

	@Override
	public Match process(Map item) throws Exception {
		Match match = new Match();
		
		Map info = (Map)item.get("info");
		Map metadata = (Map)item.get("metadata");
			
		match.setGameDuration((int) info.get("gameDuration"));
        match.setMatchId((String)metadata.get("matchId"));
        match.setGameEndTimestamp((long) info.get("gameEndTimestamp"));
        match.setQueueId((int) info.get("queueId"));
        
        String version = (String)info.get("gameVersion");
        String s = version.substring(0,2);
        int season = Integer.parseInt(s);
        match.setSeason(season);
        
        
		List<Map> participants = (ArrayList)info.get("participants");
        for(Map participant : participants) {
        	Member member = new Member();
        	
        	member.setMatch(match); //양방향 매핑 되어있어서 match 객체에 member객체 매핑할 필요 없음
        	
        	member.setCk(new MemberCompKey((String)participant.get("summonerId"),(String)metadata.get("matchId")));
        	member.setName((String)participant.get("summonerName"));
        	member.setChampionid((String)participant.get("championName"));
        	member.setPositions((String)participant.get("teamPosition"));
        	member.setWins((boolean)participant.get("win"));
        	
        	member.setTeam((int)participant.get("teamId"));
        	member.setChampLevel((int)participant.get("champLevel"));
        	member.setCs((int)participant.get("totalMinionsKilled")+(int)participant.get("neutralMinionsKilled"));
        	member.setGold((int)participant.get("goldEarned"));
        	member.setBountylevel((int)participant.get("bountyLevel")); //현상금 레벨
        	
        	member.setKills((int)participant.get("kills"));
        	member.setDeaths((int)participant.get("deaths"));
        	member.setAssists((int)participant.get("assists"));
        	
        	member.setVisionWardsBoughtInGame((int)participant.get("visionWardsBoughtInGame"));
        	member.setVisionscore((int)participant.get("visionScore")); //시야점수
        	member.setWardpalced((int)participant.get("wardsPlaced"));
        	member.setWardkill((int)participant.get("wardsKilled"));
        	member.setDetectorwardplaced((int)participant.get("detectorWardsPlaced")); //제어와드 설치 횟수
        	
        	member.setBaronkills((int)participant.get("baronKills"));
        	member.setDragonkills((int)participant.get("dragonKills"));
        	member.setInhibitorkills((int)participant.get("inhibitorKills"));
        	member.setNexuskills((int)participant.get("nexusKills"));
        	
        	member.setDoublekills((int)participant.get("doubleKills"));
        	member.setTriplekills((int)participant.get("tripleKills"));
        	member.setQuadrakills((int)participant.get("quadraKills"));
        	member.setPentakills((int)participant.get("pentaKills"));
        	
        	member.setItem0((int)participant.get("item0"));
        	member.setItem1((int)participant.get("item1"));
        	member.setItem2((int)participant.get("item2"));
        	member.setItem3((int)participant.get("item3"));
        	member.setItem4((int)participant.get("item4"));
        	member.setItem5((int)participant.get("item5"));
        	member.setItem6((int)participant.get("item6"));	
        	
        }
        
		return match;
	}

}
