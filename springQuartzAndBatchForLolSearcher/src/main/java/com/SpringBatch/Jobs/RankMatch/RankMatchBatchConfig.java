package com.SpringBatch.Jobs.RankMatch;

import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.SpringBatch.Entity.match.Match;



@Configuration
@SuppressWarnings("rawtypes")
public class RankMatchBatchConfig {
	
	private static final int chunksize = 1;
	
	private final JobBuilderFactory jobBuilderFactory;
	
	private final StepBuilderFactory stepBuilderFactory;
	
	private final EntityManagerFactory entityManagerFactory;
	
	private final WebClient.Builder webclientBuilder;
	
	
	public RankMatchBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			EntityManagerFactory entityManagerFactory, WebClient.Builder webclientBuilder) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.entityManagerFactory = entityManagerFactory;
		this.webclientBuilder = webclientBuilder;
	}

	@Bean
	public WebClient webClient() {
		return webclientBuilder.build();
	}
	
	@Bean
	public Job matchJob() {
		return jobBuilderFactory.get("matchesJob")
				.start(matchStep())
				.build();
	}
	
	@Bean
	@JobScope
	public Step matchStep() {
		return stepBuilderFactory.get("matchesStep")
				.<Map,Match>chunk(new RestApiCompletionPolicy())
				.reader(restApiMatchesReader(webClient(), null))
				.processor(jsonToEntityProcesser())
				.writer(jpaItemWriter())
				.build();
	}

	@Bean
	@StepScope
	public RestApiMatchesReader restApiMatchesReader(WebClient webClient,
			@Value("#{jobParameters[matchId]}")String matchid) {
		
		RestApiMatchesReader reader = new RestApiMatchesReader(webClient, matchid);
			
		return reader;
	}

	@Bean
	public JsonToEntityItemProcessor jsonToEntityProcesser() {
		JsonToEntityItemProcessor itemProcessor = new JsonToEntityItemProcessor();
		return itemProcessor;
	}
	
	
	@Bean
	public JpaItemWriter<Match> jpaItemWriter(){
		JpaItemWriterBuilder<Match> jpaItemWriterBuilder = new JpaItemWriterBuilder<>();
		
		return jpaItemWriterBuilder
				.entityManagerFactory(entityManagerFactory)
				.build();
	}

}
