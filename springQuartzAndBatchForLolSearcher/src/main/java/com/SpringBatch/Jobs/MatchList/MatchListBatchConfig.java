package com.SpringBatch.Jobs.MatchList;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MatchListBatchConfig {

	private static final int chunksize = 1;
	
	private final JobBuilderFactory jobBuilderFactory;
	
	private final StepBuilderFactory stepBuilderFactory;
	
	private final EntityManagerFactory entityManagerFactory;
	
	private final WebClient webclient;
	
	public MatchListBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			EntityManagerFactory entityManagerFactory, WebClient webclient) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.entityManagerFactory = entityManagerFactory;
		this.webclient = webclient;
	}
	
	
	@Bean
	public Job matchListJob() {
		return jobBuilderFactory.get("matchListJob")
				.start(matchListStep())
				.build();
	}
	
	@Bean
	@JobScope
	public Step matchListStep() {
		return stepBuilderFactory.get("matchListStep")
				.<List<String>,String>chunk(chunksize)
				.reader(restApiMatchListReader(webclient, null))
				.processor(customMemoryProcesser(null))
				.writer(customJpaItemWriter(null))
				.build();
	}
	
	@Bean
	@StepScope
	public RestApiMatchListReader restApiMatchListReader(WebClient webClient,
			@Value("#{jobParameters[puuId]}")String puuId) {
		
		RestApiMatchListReader reader = new RestApiMatchListReader(webClient, puuId);
			
		return reader;
	}
	
	@Bean
	@StepScope
	public CustomMemoryMatchIdItemProcessor customMemoryProcesser(
			@Value("#{jobParameters[lastMatchId]}")String lastMatchId) {
		return new CustomMemoryMatchIdItemProcessor(lastMatchId);
	}
	
	@Bean
	@StepScope
	public CumstomJpaItemWriter customJpaItemWriter(
			@Value("#{jobParameters[puuId]}")String summonerId){
		
		CumstomJpaItemWriter writer = new CumstomJpaItemWriter(summonerId);
		writer.setEntityManagerFactory(entityManagerFactory);
		
		return writer;
	}
}
