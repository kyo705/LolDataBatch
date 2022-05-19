package com.SpringBatch.Jobs.puuids;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.SpringBatch.Entity.Summoner;

@Configuration
public class PuuidsBatchConfig {
private static final int chunksize = 2000;
	
	private final JobBuilderFactory jobBuilderFactory;
	
	private final StepBuilderFactory stepBuilderFactory;
	
	private final EntityManagerFactory entityManagerFactory;

	public PuuidsBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			EntityManagerFactory entityManagerFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.entityManagerFactory = entityManagerFactory;
	}
	
	@Bean
	public Job jobBuilderFactory() {
		return jobBuilderFactory.get("puuIdsJob")
				.start(stepBuilderFactory())
				.build();
	}
	
	@Bean
	public Step stepBuilderFactory() {
		return stepBuilderFactory.get("puuIdsStep")
				.<Summoner,Summoner>chunk(chunksize)
				.reader(jpaPagingItemReader())
				.writer(customMemoryItemWriter())
				.build();
	}

	@Bean
	public JpaPagingItemReader<Summoner> jpaPagingItemReader() {
		String queryString = "SELECT s FROM Summoner s "
				+ "WHERE s.id IN (SELECT r.ck.summonerId FROM Rank r "
				+ "WHERE r.ck.queueType = 'RANKED_SOLO_5x5' AND "
				+ "r.tier IN ('PLATINUM','DIAMOND','MASTER','GRANDMASTER','CHALLENGER'))";
		
		
		return new JpaPagingItemReaderBuilder<Summoner>()
				.name("summonerItemReader")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(chunksize)
				.queryString(queryString)
				.build();

	}
	
	@Bean
	public CustomMemoryPuuIdItemWriter customMemoryItemWriter() {
		
		return new CustomMemoryPuuIdItemWriter();
	}
}
