package com.SpringBatch.Jobs.championstatic;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.SpringBatch.Entity.match.Match;
import com.SpringBatch.Entity.temporarychapion.TemporaryChampion;
import com.SpringBatch.Entity.temporarychapion.chamionitem.TemporaryChampItem;
import com.SpringBatch.Entity.temporarychapion.championEnemy.TemporaryChampEnemy;
import com.SpringBatch.listener.ChampStaticItemWriterListener;
import com.SpringBatch.listener.ChampStaticStepExecutionListener;
import com.SpringBatch.paramvalidator.ChampStaticJobParamValidator;

@Configuration
public class ChampStaticBatchConfig {

	EntityManagerFactory emf;
	JobBuilderFactory jbf;
	StepBuilderFactory sbf;
	
	private static final int chunkSize = 200;

	public ChampStaticBatchConfig(EntityManagerFactory emf, 
			JobBuilderFactory jbf, 
			StepBuilderFactory sbf) {
		this.emf = emf;
		this.jbf = jbf;
		this.sbf = sbf;
	}
	
	@Bean
	public Job champStaticJob() {
		return jbf.get("champStaticJob")
				.validator(champStaticParamValidator()) 
				.start(champStaticStep1())   //통계 데이터 임시 저장소에 저장
				.on("STOPPED").stopAndRestart(champStaticStep1())
				.from(champStaticStep1())
				.on("*").to(parallelFlow()) //기존 데이터와 신규 데이터를 통합
				.end()
				.build();
	}
	
	@Bean
	public Step champStaticStep1() {
		return sbf.get("champStaticStep")
				.<Match,Match>chunk(chunkSize)
				.reader(champStaticJpaPagingItemReader(null, null, null))
				.writer(champStaticJpaItemWriter())
				.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Match> champStaticJpaPagingItemReader(
			@Value("#{jobParameters[currentTimeStamp]}")Long currentTimeStamp,
			@Value("#{jobParameters[queueId]}")Long queueId,
			@Value("#{jobParameters[seasonId]}")Long seasonId) {
		
		int queueid = queueId.intValue();
		int seasonid = seasonId.intValue();
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("startDatetime", currentTimeStamp - 1000*60*60*24 + 1);
		parameters.put("endDatetime", currentTimeStamp);
		parameters.put("queueId", queueid);
		parameters.put("seasonId", seasonid);
		
		return new JpaPagingItemReaderBuilder<Match>()
				.name("dbChampItemReader")
				.entityManagerFactory(emf)
				.pageSize(chunkSize)
				.queryString("SELECT m FROM Match m "
						+ "WHERE (m.gameEndTimestamp BETWEEN :startDatetime AND :endDatetime) "
						+ "AND m.queueId = :queueId "
						+ "AND m.season = :seasonId "
						+ "ORDER BY m.matchid")
				.parameterValues(parameters)
				.build();
	}
	
	@Bean
	public ChampStaticJpaItemWriter champStaticJpaItemWriter() {
		ChampStaticJpaItemWriter itemWriter = new ChampStaticJpaItemWriter();
		itemWriter.setEntityManagerFactory(emf);
		return itemWriter;
	}
	
	@Bean
	public ChampStaticStepExecutionListener champStaticStepExecutionListener() {
		return new ChampStaticStepExecutionListener();
	}
	
	@Bean
	@StepScope
	public ChampStaticItemWriterListener champStaticItemWriterListener(
			@Value("#{jobParameters[currentTimeStamp]}")Long currentTimeStamp,
			@Value("#{jobParameters[queueId]}")Long queueId,
			@Value("#{jobParameters[seasonId]}")Long seasonId) {
		int queueid = queueId.intValue();
		int seasonid = seasonId.intValue();
		
		return new ChampStaticItemWriterListener(currentTimeStamp, queueid, seasonid);
	}
	
	@Bean
	public CompositeJobParametersValidator champStaticParamValidator() {
		CompositeJobParametersValidator compositeValidator = new CompositeJobParametersValidator();
		
		DefaultJobParametersValidator defaultValidator = 
				new DefaultJobParametersValidator(
						new String[] {"currentTimeStamp","queueId","seasonId"}, 
						new String[] {});
		
		defaultValidator.afterPropertiesSet();
		
		
		ChampStaticJobParamValidator champStaticValidator = new ChampStaticJobParamValidator();
		
		compositeValidator.setValidators(
				Arrays.asList(defaultValidator, champStaticValidator));
		
		return compositeValidator;
	}

	@Bean
	public Flow parallelFlow() {
		return new FlowBuilder<Flow>("parallelFlow")
				.start(champStaticFlow())
				.split(new SimpleAsyncTaskExecutor())
				.add(champItemStaticFlow())
				.split(new SimpleAsyncTaskExecutor())
				.add(champEnemyStaticFlow())
				.build();
	}
	
	@Bean
	public Flow champStaticFlow() {
		return new FlowBuilder<Flow>("champStaticFlow")
				.start(savingChampStaticStep())
				.build();
	}
	
	@Bean
	public Flow champItemStaticFlow() {
		return new FlowBuilder<Flow>("champItemStaticFlow")
				.start(savingChampItemStaticStep())
				.build();
	}

	@Bean
	public Flow champEnemyStaticFlow() {
		return new FlowBuilder<Flow>("champEnemyStaticFlow")
				.start(savingChampEnemyStaticStep())
				.build();
	}

	
	@Bean
	public Step savingChampStaticStep() {
		return sbf.get("savingChampStaticStep")
				.<TemporaryChampion, TemporaryChampion>chunk(chunkSize)
				.reader(savingChampStaticJpaPagingItemReader())
				.writer(savingChampStaticJpaPagingItemWriter())
				.build();
	}

	@Bean
	public Step savingChampItemStaticStep() {
		return sbf.get("savingChampItemStaticStep")
				.<TemporaryChampItem, TemporaryChampItem>chunk(chunkSize)
				.reader(savingChampItemStaticJpaPagingItemReader())
				.writer(savingChampItemStaticJpaPagingItemWriter())
				.build();
	}
	
	@Bean
	public Step savingChampEnemyStaticStep() {
		return sbf.get("savingChampEnemyStaticStep")
				.<TemporaryChampEnemy, TemporaryChampEnemy>chunk(chunkSize)
				.reader(savingChampEnemyStaticJpaPagingItemReader())
				.writer(savingChampEnemyStaticJpaPagingItemWriter())
				.build();
	}
	
	@Bean
	public JpaPagingItemReader<TemporaryChampion> savingChampStaticJpaPagingItemReader() {
		String jpql = "select c from TemporaryChampion c";
		
		return new JpaPagingItemReaderBuilder<TemporaryChampion>()
				.name("savingChampStaticJpaPagingItemReader")
				.entityManagerFactory(emf)
				.pageSize(chunkSize)
				.queryString(jpql)
				.build();
	}
	
	@Bean
	public JpaPagingItemReader<TemporaryChampItem> savingChampItemStaticJpaPagingItemReader() {
		String jpql = "select c from TemporaryChampItem c";
		
		return new JpaPagingItemReaderBuilder<TemporaryChampItem>()
				.name("savingChampItemStaticJpaPagingItemReader")
				.entityManagerFactory(emf)
				.pageSize(chunkSize)
				.queryString(jpql)
				.build();
	}
	
	@Bean
	public JpaPagingItemReader<TemporaryChampEnemy> savingChampEnemyStaticJpaPagingItemReader() {
		String jpql = "select c from TemporaryChampEnemy c";
		
		return new JpaPagingItemReaderBuilder<TemporaryChampEnemy>()
				.name("savingChampEnemyStaticJpaPagingItemReader")
				.entityManagerFactory(emf)
				.pageSize(chunkSize)
				.queryString(jpql)
				.build();
	}
	
	@Bean
	public SavingChampStaticJpaItemWriter savingChampStaticJpaPagingItemWriter() {
		SavingChampStaticJpaItemWriter itemWriter = new SavingChampStaticJpaItemWriter();
		itemWriter.setEntityManagerFactory(emf);
		return itemWriter;
	}
	
	@Bean
	public SavingChampItemStaticJpaItemWriter savingChampItemStaticJpaPagingItemWriter() {
		SavingChampItemStaticJpaItemWriter itemWriter = new SavingChampItemStaticJpaItemWriter();
		itemWriter.setEntityManagerFactory(emf);
		return itemWriter;
	}

	@Bean
	public SavingChampEnemyStaticJpaItemWriter savingChampEnemyStaticJpaPagingItemWriter() {
		SavingChampEnemyStaticJpaItemWriter itemWriter = new SavingChampEnemyStaticJpaItemWriter();
		itemWriter.setEntityManagerFactory(emf);
		return itemWriter;
	}
}
