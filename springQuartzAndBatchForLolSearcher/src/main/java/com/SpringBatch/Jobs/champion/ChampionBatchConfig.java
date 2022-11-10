package com.SpringBatch.Jobs.champion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.SpringBatch.Entity.match.Match;
import com.SpringBatch.Entity.temporarychapion.TemporaryChampion;
import com.SpringBatch.Entity.temporarychapion.chamionitem.TemporaryChampItem;
import com.SpringBatch.Entity.temporarychapion.championEnemy.TemporaryChampEnemy;
import com.SpringBatch.listener.ChampionItemWriterListener;
import com.SpringBatch.listener.ChampionStepExecutionListener;
import com.SpringBatch.paramvalidator.ChampionJobParamValidator;

@Configuration
public class ChampionBatchConfig {
	private final int chunkSize = 200;
	private final String champStaticJobName = "championJob";
	private final String champStaticStepName = "championStep";
	
	EntityManagerFactory emf;
	JobBuilderFactory jbf;
	StepBuilderFactory sbf;

	public ChampionBatchConfig(EntityManagerFactory emf, 
			JobBuilderFactory jbf, 
			StepBuilderFactory sbf) {
		this.emf = emf;
		this.jbf = jbf;
		this.sbf = sbf;
	}
	
	@Qualifier("champStaticJob")
	@Bean
	public Job championJob() {
		return jbf.get(champStaticJobName)
				.validator(championParamValidator()) 
				.start(championStep())   //통계 데이터 임시 저장소에 저장
				.on("STOPPED").stopAndRestart(championStep())
				.from(championStep())
				.on("*").to(integrationParallelFlow()) //기존 데이터와 신규 데이터를 통합
				.end()
				.build();
	}
	
	@Bean
	public Step championStep() {
		return sbf.get(champStaticStepName)
				.<Match,Match>chunk(chunkSize)
				.reader(championJpaPagingItemReader(null, null, null))
				.writer(championJpaItemWriter())
				.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Match> championJpaPagingItemReader(
			@Value("#{jobParameters[currentTimeStamp]}")Long currentTimeStamp,
			@Value("#{jobParameters[queueId]}")Long queueId,
			@Value("#{jobParameters[seasonId]}")Long seasonId) {
		
		String jpql = "SELECT m FROM Match m "
				+ "WHERE m.queueId = :queueId AND m.season = :seasonId "
				+ "AND (m.gameEndTimestamp BETWEEN :startDatetime AND :endDatetime) "
				+ "ORDER BY m.matchid";
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("startDatetime", currentTimeStamp - 1000*60*60*24 + 1);
		parameters.put("endDatetime", currentTimeStamp);
		parameters.put("queueId", queueId.intValue());
		parameters.put("seasonId", seasonId.intValue());
		
		return new JpaPagingItemReaderBuilder<Match>()
				.name("dbChampItemReader")
				.entityManagerFactory(emf)
				.pageSize(chunkSize)
				.queryString(jpql)
				.parameterValues(parameters)
				.build();
	}
	
	@Bean
	public ChampionJpaItemWriter championJpaItemWriter() {
		ChampionJpaItemWriter itemWriter = new ChampionJpaItemWriter();
		itemWriter.setEntityManagerFactory(emf);
		return itemWriter;
	}
	
	@Bean
	public ChampionStepExecutionListener championStepExecutionListener() {
		return new ChampionStepExecutionListener();
	}
	
	@Bean
	@StepScope
	public ChampionItemWriterListener championItemWriterListener(
			@Value("#{jobParameters[currentTimeStamp]}")Long currentTimeStamp,
			@Value("#{jobParameters[queueId]}")Long queueId,
			@Value("#{jobParameters[seasonId]}")Long seasonId) {
		int queueid = queueId.intValue();
		int seasonid = seasonId.intValue();
		
		return new ChampionItemWriterListener(currentTimeStamp, queueid, seasonid);
	}
	
	@Bean
	public CompositeJobParametersValidator championParamValidator() {
		DefaultJobParametersValidator defaultValidator = 
				new DefaultJobParametersValidator(
						new String[] {"currentTimeStamp","queueId","seasonId"}, 
						new String[] {});
		defaultValidator.afterPropertiesSet();
		
		ChampionJobParamValidator champStaticValidator = new ChampionJobParamValidator();
		
		CompositeJobParametersValidator compositeValidator = new CompositeJobParametersValidator();
		compositeValidator.setValidators(
				Arrays.asList(defaultValidator, champStaticValidator));
		
		return compositeValidator;
	}

	@Bean
	public Flow integrationParallelFlow() {
		return new FlowBuilder<Flow>("parallelFlow")
				.start(championPositionFlow())
				.split(new SimpleAsyncTaskExecutor())
				.add(championItemFlow())
				.split(new SimpleAsyncTaskExecutor())
				.add(championEnemyFlow())
				.build();
	}
	
	@Bean
	public Flow championPositionFlow() {
		return new FlowBuilder<Flow>("champPositionFlow")
				.start(savingChampPositionStep())
				.build();
	}
	
	@Bean
	public Flow championItemFlow() {
		return new FlowBuilder<Flow>("champItemFlow")
				.start(savingChampItemStep())
				.build();
	}

	@Bean
	public Flow championEnemyFlow() {
		return new FlowBuilder<Flow>("champEnemyFlow")
				.start(savingChampEnemyStep())
				.build();
	}

	
	@Bean
	public Step savingChampPositionStep() {
		return sbf.get("savingChampPositionStep")
				.<TemporaryChampion, TemporaryChampion>chunk(chunkSize)
				.reader(savingChampionPositionJpaPagingItemReader())
				.writer(savingChampionPositionJpaPagingItemWriter())
				.build();
	}

	@Bean
	public Step savingChampItemStep() {
		return sbf.get("savingChampItemStep")
				.<TemporaryChampItem, TemporaryChampItem>chunk(chunkSize)
				.reader(savingChampionItemJpaPagingItemReader())
				.writer(savingChampionItemJpaPagingItemWriter())
				.build();
	}
	
	@Bean
	public Step savingChampEnemyStep() {
		return sbf.get("savingChampEnemyStep")
				.<TemporaryChampEnemy, TemporaryChampEnemy>chunk(chunkSize)
				.reader(savingChampionEnemyJpaPagingItemReader())
				.writer(savingChampionEnemyJpaPagingItemWriter())
				.build();
	}
	
	@Bean
	public JpaPagingItemReader<TemporaryChampion> savingChampionPositionJpaPagingItemReader() {
		String jpql = "select c from TemporaryChampion c";
		
		return new JpaPagingItemReaderBuilder<TemporaryChampion>()
				.name("savingChampPositionJpaPagingItemReader")
				.entityManagerFactory(emf)
				.pageSize(chunkSize)
				.queryString(jpql)
				.build();
	}
	
	@Bean
	public JpaPagingItemReader<TemporaryChampItem> savingChampionItemJpaPagingItemReader() {
		String jpql = "select c from TemporaryChampItem c";
		
		return new JpaPagingItemReaderBuilder<TemporaryChampItem>()
				.name("savingChampItemJpaPagingItemReader")
				.entityManagerFactory(emf)
				.pageSize(chunkSize)
				.queryString(jpql)
				.build();
	}
	
	@Bean
	public JpaPagingItemReader<TemporaryChampEnemy> savingChampionEnemyJpaPagingItemReader() {
		String jpql = "select c from TemporaryChampEnemy c";
		
		return new JpaPagingItemReaderBuilder<TemporaryChampEnemy>()
				.name("savingChampEnemyJpaPagingItemReader")
				.entityManagerFactory(emf)
				.pageSize(chunkSize)
				.queryString(jpql)
				.build();
	}
	
	@Bean
	public SavingChampPositionJpaItemWriter savingChampionPositionJpaPagingItemWriter() {
		SavingChampPositionJpaItemWriter itemWriter = new SavingChampPositionJpaItemWriter();
		itemWriter.setEntityManagerFactory(emf);
		return itemWriter;
	}
	
	@Bean
	public SavingChampItemJpaItemWriter savingChampionItemJpaPagingItemWriter() {
		SavingChampItemJpaItemWriter itemWriter = new SavingChampItemJpaItemWriter();
		itemWriter.setEntityManagerFactory(emf);
		return itemWriter;
	}

	@Bean
	public SavingChampEnemyJpaItemWriter savingChampionEnemyJpaPagingItemWriter() {
		SavingChampEnemyJpaItemWriter itemWriter = new SavingChampEnemyJpaItemWriter();
		itemWriter.setEntityManagerFactory(emf);
		return itemWriter;
	}
}
