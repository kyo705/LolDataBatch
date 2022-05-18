package com.SpringBatch.Jobs.ChampEnemyStatic;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.SpringBatch.Entity.Match;

@Configuration
public class ChampEnemyStaticBatchConfig {

	EntityManagerFactory emf;
	JobBuilderFactory jbf;
	StepBuilderFactory sbf;
	
	private static final int chunckSize = 200;

	public ChampEnemyStaticBatchConfig(EntityManagerFactory emf, JobBuilderFactory jbf, StepBuilderFactory sbf) {
		super();
		this.emf = emf;
		this.jbf = jbf;
		this.sbf = sbf;
	}
	
	@Bean
	public Job champEnemyStaticJob() {
		return jbf.get("champEnemyStaticJob")
				.start(champEnemyStaticStep())
				.build();
	}

	@Bean
	public Step champEnemyStaticStep() {
		return sbf.get("champEnemyStaticStep")
				.<Match,Match>chunk(chunckSize)
				.reader(champEnemyJpaPagingItemReader(null, null))
				.writer(champEnemyJpaItemWriter())
				.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Match> champEnemyJpaPagingItemReader(
			@Value("#{jobParameters[currentTimeStamp]}")Long currentTimeStamp,
			@Value("#{jobParameters[queueId]}")Long queueId) {
		
		int queueId_ = queueId.intValue();
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("startDatetime", currentTimeStamp - 1000*60*60*24 + 1);
		parameters.put("endDatetime", currentTimeStamp);
		parameters.put("queueId", queueId_);
		
		return new JpaPagingItemReaderBuilder<Match>()
				.name("dbChampItemReader")
				.entityManagerFactory(emf)
				.pageSize(chunckSize)
				.queryString("SELECT DISTINCT m FROM Match m join fetch m.members "
						+ "WHERE (m.gameEndTimestamp BETWEEN :startDatetime AND :endDatetime) "
						+ "AND m.queueId = :queueId")
				.parameterValues(parameters)
				.build();
	}
	
	@Bean
	public champEnemyJpaItemWriter champEnemyJpaItemWriter() {
		champEnemyJpaItemWriter itemWriter = new champEnemyJpaItemWriter();
		itemWriter.setEntityManagerFactory(emf);
		return itemWriter;
	}
}
