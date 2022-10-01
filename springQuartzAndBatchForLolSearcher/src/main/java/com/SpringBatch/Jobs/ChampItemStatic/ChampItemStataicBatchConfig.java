package com.SpringBatch.Jobs.ChampItemStatic;

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
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.SpringBatch.Entity.match.Member;
import com.SpringBatch.paramvalidator.ChampStaticJobParamValidator;

@Configuration
public class ChampItemStataicBatchConfig {
	EntityManagerFactory emf;
	JobBuilderFactory jbf;
	StepBuilderFactory sbf;
	
	private static final int chunckSize = 200;

	public ChampItemStataicBatchConfig(EntityManagerFactory emf, JobBuilderFactory jbf, StepBuilderFactory sbf) {
		super();
		this.emf = emf;
		this.jbf = jbf;
		this.sbf = sbf;
	}
	
	@Bean
	public Job champItemStaticJob() {
		return jbf.get("champItemStaticJob")
				.start(champItemStaticStep())
				.validator(champStaticParamValidator())
				.build();
		
	}

	@Bean
	public Step champItemStaticStep() {
		return sbf.get("champItemStaticStep")
				.<Member,Member>chunk(chunckSize)
				.reader(ChampItemJpaPagingItemReader(null,null))
				.writer(champItemJpaItemWriter(null))
				.build();
	}
	
	@Bean
	@StepScope
	public JpaPagingItemReader<Member> ChampItemJpaPagingItemReader(
			@Value("#{jobParameters[currentTimeStamp]}")Long currentTimeStamp,
			@Value("#{jobParameters[queueId]}")Long queueId) {
		
		String query = "SELECT DISTINCT k FROM Member k inner join k.match m "
				+ "WHERE (m.gameEndTimestamp BETWEEN :startDatetime AND :endDatetime) AND m.queueId = :queueId "
				+ "ORDER BY k.ck DESC";
		
		int queueId_ = queueId.intValue();
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("startDatetime", currentTimeStamp - 1000*60*60*24 + 1);
		parameters.put("endDatetime", currentTimeStamp);
		parameters.put("queueId", queueId_);
		
		return new JpaPagingItemReaderBuilder<Member>()
				.name("dbChampItemReader")
				.entityManagerFactory(emf)
				.pageSize(chunckSize)
				.queryString(query)
				.parameterValues(parameters)
				.build();
	}
	
	@Bean
	@StepScope
	public ChampItemJpaItemWriter champItemJpaItemWriter(@Value("#{jobParameters[seasonId]}")Long seasonId){
		
		ChampItemJpaItemWriter itemWriter = new ChampItemJpaItemWriter(seasonId.intValue());
		itemWriter.setEntityManagerFactory(emf);
		
		return itemWriter;
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
}
