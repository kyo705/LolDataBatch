package com.SpringBatch.Jobs.MatchList;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.web.reactive.function.client.WebClient;

public class RestApiMatchListReader implements ItemReader<List<String>> {

	private static final String key = "RGAPI-2a0ac3ef-7f65-4854-97d4-54e2c7b3dbab";
	
	private WebClient webClient;
	
	private String puuId;
	
	private boolean used = false;
	
	public RestApiMatchListReader(WebClient webClient, String puuId) {
		this.webClient = webClient;
		this.puuId = puuId;
	}

	@Override
	public List<String> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		String[] matchIds = null;
		String uri = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/"
				+ puuId + "/ids?queue=420&start=0&count=100&api_key="+key;
		
		if(used==false) {
			matchIds = webClient.get()
					.uri(uri)
					.retrieve()
					.bodyToMono(String[].class)
					.block();
			used = true;
			
			return Arrays.asList(matchIds);
		}else {
			return null;
		}
		
		
	}

}
