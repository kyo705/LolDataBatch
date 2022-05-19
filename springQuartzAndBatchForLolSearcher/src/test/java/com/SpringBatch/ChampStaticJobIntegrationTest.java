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

import com.SpringBatch.Entity.Champion.Champion;
import com.SpringBatch.Entity.match.Match;
import com.SpringBatch.Entity.match.Member;
import com.SpringBatch.Entity.match.MemberCompKey;
import com.SpringBatch.Jobs.ChampStatic.ChampStaticBatchConfig;
import com.SpringBatch.repository.ChampionRepository;
import com.SpringBatch.repository.MatchRepository;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes= {ChampStaticBatchConfig.class, TestBatchConfig.class})
public class ChampStaticJobIntegrationTest {
	
	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    
    @Autowired
    private ChampionRepository champRepository;
    
    @Autowired
    private MatchRepository matchRepository;
    
    @AfterEach
    public void afterTest() throws Exception {
    	//테스트 코드에 의해 h2 DB에 생성된 데이터들을 삭제해주는 작업
    	matchRepository.deleteAll();
    	champRepository.deleteAll();
    	jobRepositoryTestUtils.removeJobExecutions();
    }
    
    @Test
    public void testChampItemStaticJob() throws Exception {
    	//given
    	Match match1 = new Match();
    	match1.setMatchId("600");
    	match1.setQueueId(420);
    	match1.setGameEndTimestamp(System.currentTimeMillis()-300);
    	List<Member> members1 = match1.getMembers();
    	for(int i=0;i<10;i++) {
    		members1.add(new Member());
    	}
    	for(int i=0;i<10;i++) {
    		members1.get(i).setCk(new MemberCompKey(i+"300", match1.getMatchId()));
    		members1.get(i).setMatch(match1);
    		if(i<5) 
    			members1.get(i).setWins(true);
    		else 
    			members1.get(i).setWins(false);
    	}
    	members1.get(0).setChampionid("탈론");  		
    	members1.get(1).setChampionid("그레이브즈");
    	members1.get(2).setChampionid("카타리나");
    	members1.get(3).setChampionid("케이틀린");
    	members1.get(4).setChampionid("나미");
    	members1.get(5).setChampionid("오른");
    	members1.get(6).setChampionid("카직스");
    	members1.get(7).setChampionid("오리아나");
    	members1.get(8).setChampionid("징크스");
    	members1.get(9).setChampionid("알리스타");
    	
    	members1.get(0).setPositions("TOP");
    	members1.get(1).setPositions("JUNGLE");
    	members1.get(2).setPositions("MIDDLE");
    	members1.get(3).setPositions("BOTTOM");
    	members1.get(4).setPositions("UTILITY");
    	members1.get(5).setPositions("TOP");
    	members1.get(6).setPositions("JUNGLE");
    	members1.get(7).setPositions("MIDDLE");
    	members1.get(8).setPositions("BOTTOM");
    	members1.get(9).setPositions("UTILITY");
    	
    	Match match2 = new Match();
    	match2.setMatchId("700");
    	match2.setQueueId(420);
    	match2.setGameEndTimestamp(System.currentTimeMillis()-200);
    	List<Member> members2 = match2.getMembers();
    	for(int i=0;i<10;i++) {
    		members2.add(new Member());
    	}
    	for(int i=0;i<10;i++) {
    		members2.get(i).setCk(new MemberCompKey(i+"300", match2.getMatchId()));
    		members2.get(i).setMatch(match2);
    		if(i<5) 
    			members2.get(i).setWins(true);
    		else 
    			members2.get(i).setWins(false);
    	}
    	members2.get(0).setChampionid("탈론");  		
    	members2.get(1).setChampionid("그레이브즈");
    	members2.get(2).setChampionid("카타리나");
    	members2.get(3).setChampionid("케이틀린");
    	members2.get(4).setChampionid("나미");
    	members2.get(5).setChampionid("오른");
    	members2.get(6).setChampionid("니달리");
    	members2.get(7).setChampionid("제드");
    	members2.get(8).setChampionid("징크스");
    	members2.get(9).setChampionid("알리스타");
    	
    	members2.get(0).setPositions("TOP");
    	members2.get(1).setPositions("JUNGLE");
    	members2.get(2).setPositions("MIDDLE");
    	members2.get(3).setPositions("BOTTOM");
    	members2.get(4).setPositions("UTILITY");
    	members2.get(5).setPositions("TOP");
    	members2.get(6).setPositions("JUNGLE");
    	members2.get(7).setPositions("MIDDLE");
    	members2.get(8).setPositions("BOTTOM");
    	members2.get(9).setPositions("UTILITY");
    	
    	matchRepository.save(match1);
    	matchRepository.save(match2);
    	
    	JobParameters jobParameters = new JobParametersBuilder()
    			.addLong("currentTimeStamp", System.currentTimeMillis())
    			.addLong("queueId", 420L)
    			.toJobParameters();
    	
    	//when
    	JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
    	
    	//then
    	assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    	List<Champion> champions = champRepository.findAll();
    	int totalWinCount = 0;
    	int totalLossCount = 0;
    	for(Champion champion : champions) {
    		System.out.println(champion.getCk().getChampionId()+" "+champion.getCk().getPosition()+" "+champion.getWins()+"/"+champion.getLosses());
    		totalWinCount += champion.getWins();
    		totalLossCount += champion.getLosses();
    	}
    	assertThat(totalWinCount-totalLossCount).isEqualTo(0);
    	
    }
}
