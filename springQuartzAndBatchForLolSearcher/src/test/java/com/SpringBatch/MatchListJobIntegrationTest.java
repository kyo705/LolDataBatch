package com.SpringBatch;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import com.Quartz.QuartzJob.MatchesQuartzJob;
import com.SpringBatch.Entity.Summoner;
import com.SpringBatch.Jobs.MatchList.MatchListBatchConfig;
import com.SpringBatch.repository.SummonerRepository;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes= {TestBatchConfig.class, MatchListBatchConfig.class, MatchesQuartzJob.class})
public class MatchListJobIntegrationTest {

	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    
    @Autowired
    private SummonerRepository summonerRepository;
    
    @Autowired
	ApplicationContext applicationContext;
    
    @AfterEach
    public void afterTest() throws Exception {
    	summonerRepository.deleteAll();
    	jobRepositoryTestUtils.removeJobExecutions();
    }
    
    @Test
    public void testMatchListJob() throws Exception {
    	//given
    	Summoner summoner = new Summoner();
    	summoner.setLastmatchid("KR_5932101871");
    	summoner.setName("¿ªÃµ±«");
    	summoner.setPuuid("mJba5BtTv6mLhAOhV8mVqADXfA_hUb8DvROL8qdre0g4aRFfq4wh7S_SMVYdVpA0G1df3YsggcTlRg");
    	summoner.setId("3jQVa-lOPsGJrwOXu3zNUMKsIKmgr90s_jC9-2AK_KBfdCU");
    	
    	summonerRepository.save(summoner);
    	
    	JobParameters jobParameters = new JobParametersBuilder()
				.addString("puuId", summoner.getPuuid())
				.addString("lastMatchId", summoner.getLastmatchid())
				.toJobParameters();
    	
    	//when
    	JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
    	
    	
    	//then
    	assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    	MatchesQuartzJob matchesQuartzJob = applicationContext.getBean(MatchesQuartzJob.class);
    	assertThat(matchesQuartzJob.matchIds.size()).isNotEqualTo(0);
    	
    	List<Summoner> summoners = summonerRepository.findAll();
    	assertThat(summoners.size()).isEqualTo(1);
    	assertThat(summoners.get(0).getLastmatchid()).isNotEqualTo(summoner.getLastmatchid());
    	assertThat(matchesQuartzJob.matchIds.containsKey(summoners.get(0).getLastmatchid())).isEqualTo(true);
    	
    }
}
