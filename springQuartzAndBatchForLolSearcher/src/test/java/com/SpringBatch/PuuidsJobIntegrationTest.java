package com.SpringBatch;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.SpringBatch.Entity.rank.Rank;
import com.SpringBatch.Entity.rank.RankCompKey;
import com.SpringBatch.Jobs.puuids.PuuidsBatchConfig;
import com.SpringBatch.repository.RankRepository;
import com.SpringBatch.repository.SummonerRepository;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes= {PuuidsBatchConfig.class, TestBatchConfig.class, MatchesQuartzJob.class})
public class PuuidsJobIntegrationTest {

	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    
    @Autowired
    private SummonerRepository summonerRepository;
    
    @Autowired
    private RankRepository rankRepository;
    
    @Autowired
	ApplicationContext applicationContext;
    
    @AfterEach
    public void afterTest() throws Exception {
    	//테스트 코드에 의해 h2 DB에 생성된 데이터들을 삭제해주는 작업
    	summonerRepository.deleteAll();
    	rankRepository.deleteAll();
    	jobRepositoryTestUtils.removeJobExecutions();
    }
    
    @Test
    public void testPuuidsJob() throws Exception {
    	//given
    	Summoner summoner1 = new Summoner();
    	summoner1.setId("1");
    	summoner1.setPuuid("puuid1");
    	summoner1.setLastmatchid("match1");
    	
    	Rank rank1 = new Rank();
    	rank1.setCk(new RankCompKey(summoner1.getId(), "RANKED_SOLO_5x5"));
    	rank1.setTier("PLATINUM");
    	
    	Summoner summoner2 = new Summoner();
    	summoner2.setId("2");
    	summoner2.setPuuid("puuid2");
    	summoner2.setLastmatchid("match5");
    	
    	Rank rank2 = new Rank();
    	rank2.setCk(new RankCompKey(summoner2.getId(), "RANKED_SOLO_5x5"));
    	rank2.setTier("CHALLENGER");
    	
    	summonerRepository.save(summoner1);
    	summonerRepository.save(summoner2);
    	
    	rankRepository.save(rank1);
    	rankRepository.save(rank2);
    	
    	JobParameters jobParameters = new JobParametersBuilder()
    			.addLong("currentTimeStamp", System.currentTimeMillis())
    	    	.toJobParameters();
    	
    	//when
    	JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
    	
    	//then
    	assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    	MatchesQuartzJob matchesQuartzJob = applicationContext.getBean(MatchesQuartzJob.class);
    	assertThat(matchesQuartzJob.puuIds.get("puuid1")).isEqualTo("match1");
    	assertThat(matchesQuartzJob.puuIds.size()).isEqualTo(2);
    }
}
