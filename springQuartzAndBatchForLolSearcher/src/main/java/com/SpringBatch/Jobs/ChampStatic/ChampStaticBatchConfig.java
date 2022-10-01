package com.SpringBatch.Jobs.ChampStatic;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.SpringBatch.Entity.Champion.Champion;
import com.SpringBatch.Entity.Champion.ChampionCompKey;


@Configuration
public class ChampStaticBatchConfig {
	
	private static final int chunckSize = 20;
	
	private	EntityManagerFactory emf;
	
	private JobBuilderFactory jbf;
	
	private StepBuilderFactory sbf;
	
	public ChampStaticBatchConfig(EntityManagerFactory emf, JobBuilderFactory jbf, StepBuilderFactory sbf) {
		this.emf = emf;
		this.jbf = jbf;
		this.sbf = sbf;
	}
	
	@Bean
	public Job champStaticJob() {
		return jbf.get("champStaticJob")
				.start(champStaticStep())
				.build();
	}
	
	@Bean
	public Step champStaticStep() {
		return sbf.get("champStaticStep")
				.<Object,Champion>chunk(chunckSize)
				.reader(matchJpaPagingItemReader(null,null))
				.processor(matchToChampProcessor())
				.writer(champStaticItemWriter())
				.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Object> matchJpaPagingItemReader(
			@Value("#{jobParameters[currentTimeStamp]}")Long currentTimeStamp,
			@Value("#{jobParameters[queueId]}")Integer queueId) {
		
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("startDatetime", currentTimeStamp - 1000*60*60*24 + 1);
		parameters.put("endDatetime", currentTimeStamp);
		parameters.put("queueId", queueId);
		
		String queryString = "SELECT DISTINCT k.championid, m.season, k.positions, k.wins, count(m) "
				+ "FROM Match m INNER JOIN m.members k "
				+ "WHERE (m.gameEndTimestamp BETWEEN :startDatetime AND :endDatetime) AND m.queueId = :queueId "
				+ "GROUP BY m.season, k.championid, k.wins"
				+ "ORDER BY ";
		
		return new JpaPagingItemReaderBuilder<Object>()
				.name("dbMatchReader")
				.entityManagerFactory(emf)
				.pageSize(chunckSize)
				.queryString(queryString)
				.parameterValues(parameters)
				.build();
	}

	@Bean
	public ItemProcessor<Object, Champion> matchToChampProcessor() {
		return items ->{
			Champion champion = new Champion();
			
			Object[] item = (Object[])items;
			
			champion.setCk(new ChampionCompKey((String)item[0], (int)item[1], (String)item[2]));
			
			if((boolean)item[3]==true) {
				champion.setWins((long)item[4]);
				champion.setLosses(0);
			}else {
				champion.setWins(0);
				champion.setLosses((long)item[4]);
			}
 			
			return champion;
		};
	}

	@Bean
	public ChampStaticItemWriter champStaticItemWriter() {
		ChampStaticItemWriter jpaItemWriter = new ChampStaticItemWriter();
		jpaItemWriter.setEntityManagerFactory(emf);
		
		return jpaItemWriter;
	}
}
