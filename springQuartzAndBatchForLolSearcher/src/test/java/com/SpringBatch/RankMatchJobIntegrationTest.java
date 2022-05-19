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
import org.springframework.test.context.ActiveProfiles;

import com.SpringBatch.Jobs.RankMatch.RankMatchBatchConfig;
import com.SpringBatch.repository.MatchRepository;


@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes= {RankMatchBatchConfig.class, TestBatchConfig.class})
public class RankMatchJobIntegrationTest {
	
	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    
    @Autowired
    private MatchRepository matchRepository;
    
    @AfterEach
    public void afterTest() throws Exception {
    	//�׽�Ʈ �ڵ忡 ���� h2 DB�� ������ �����͵��� �������ִ� �۾�
    	
    	jobRepositoryTestUtils.removeJobExecutions();
    }
    
    @Test
    public void testRankMatchJob() throws Exception {
    	//given
    	JobParameters jobParameters = new JobParametersBuilder()
    			.addLong("currentTimeStamp", System.currentTimeMillis())
    	    	.toJobParameters();
    	
    	//when
    	JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
    	
    	//then
    	assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    	matchRepository.findAll();
    }
}
