package com.SpringBatch;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.SpringBatch.Entity.Match;
import com.SpringBatch.Entity.Champion.ChampionItem.ChampItem;
import com.SpringBatch.Jobs.ChampItemStatic.ChampItemStataicBatchConfig;
import com.SpringBatch.repository.ChampItemRepository;
import com.SpringBatch.repository.MatchRepository;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes= {ChampItemStataicBatchConfig.class, TestBatchConfig.class})
public class ChampItemStaticJobIntegrationTest {
	
	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    
    @Autowired
    private ChampItemRepository champItemRepository;
    
    @Autowired
    private MatchRepository matchRepository;
    
    @AfterEach
    public void afterTest() throws Exception {
    	//�׽�Ʈ �ڵ忡 ���� h2 DB�� ������ �����͵��� �������ִ� �۾�
    	matchRepository.deleteAll();
    	champItemRepository.deleteAll();
    	jobRepositoryTestUtils.removeJobExecutions();
    }
    
    @Test
    public void testChampItemStaticJob() throws Exception {
    	//given
    	Match match1 = new Match();
    	Match match2 = new Match();
    	matchRepository.save(match1);
    	matchRepository.save(match2);
    	
    	JobParameters jobParameters = new JobParametersBuilder()
    			.addLong("currentTimeStamp", null)
    			.addLong("queueId", 420L)
    			.toJobParameters();
    	
    	//when
    	JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
    	
    	//then
    	assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    	
    	List<ChampItem> champItems = champItemRepository.findAll();
    	ChampItem yasuo_infinite = null;
    	for(ChampItem champItem : champItems) {
    		if(champItem.getCk().getChampionId().equals("Yasuo")
    				&&champItem.getCk().getItemId()==521
    				&&champItem.getCk().getSeasonId()==22) {
    			yasuo_infinite = champItem;
    			break;
    		}
    	}
    	
    	assertThat(yasuo_infinite.getWins()).isEqualTo(1);
    	assertThat(yasuo_infinite.getLosses()).isEqualTo(1);
    }
}
