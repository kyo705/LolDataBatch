package com.SpringBatch.Jobs.RankMatch;


import java.util.Map;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.web.reactive.function.client.WebClient;



@SuppressWarnings("rawtypes")
public class RestApiMatchesReader implements ItemReader<Map> {

	private static final String key = "RGAPI-2a0ac3ef-7f65-4854-97d4-54e2c7b3dbab";
	
	private String matchid;
	
	private WebClient webclient;
	
	private boolean used = false;
	
	public RestApiMatchesReader(WebClient webclient, String matchid) {
		this.webclient = webclient;
		this.matchid = matchid;
	}

	@Override
	public Map read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		Map json = null;
		
		if(used==false) {
			json = webclient.get()
					.uri("https://asia.api.riotgames.com/lol/match/v5/matches/"+matchid+"?api_key="+key)
					.retrieve()
					.bodyToMono(Map.class)
					.block();
			used = true;
		}
		
		return json;
	}



}
