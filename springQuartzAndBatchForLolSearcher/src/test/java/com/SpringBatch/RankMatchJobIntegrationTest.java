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
import org.springframework.test.context.ActiveProfiles;

import com.SpringBatch.Entity.match.Match;
import com.SpringBatch.Entity.match.Member;
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
    	//테스트 코드에 의해 h2 DB에 생성된 데이터들을 삭제해주는 작업
    	matchRepository.deleteAll();
    	jobRepositoryTestUtils.removeJobExecutions();
    }
    
    @Test
    public void testRankMatchJob() throws Exception {
    	//given
    	JobParameters jobParameters = new JobParametersBuilder()
    			.addString("matchId", "KR_5932101871")
    	    	.toJobParameters();
    	
    	//when
    	JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
    	
    	//then
    	assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    	List<Match> matches = matchRepository.findAll();
    	assertThat(matches.size()).isEqualTo(1);
    	assertThat(matches.get(0).getMatchId()).isEqualTo("KR_5932101871");
    	for(Member member : matches.get(0).getMembers()) {
    		System.out.println(member.getChampionid());
    	}
    }
}
