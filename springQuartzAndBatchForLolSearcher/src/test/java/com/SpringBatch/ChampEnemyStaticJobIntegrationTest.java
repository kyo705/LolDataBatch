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

import com.SpringBatch.Entity.Champion.ChampionEnemy.ChampEnemy;
import com.SpringBatch.Entity.match.Match;
import com.SpringBatch.Entity.match.Member;
import com.SpringBatch.Entity.match.MemberCompKey;
import com.SpringBatch.Jobs.ChampEnemyStatic.ChampEnemyStaticBatchConfig;
import com.SpringBatch.repository.MatchRepository;
import com.SpringBatch.repository.championrepository.ChampionReository;
import com.SpringBatch.repository.championrepository.JpaChampionRepository;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes= {ChampEnemyStaticBatchConfig.class, TestBatchConfig.class, JpaChampionRepository.class})
public class ChampEnemyStaticJobIntegrationTest {

	private static final int seasonId = 22;
	
	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private ChampionReository jpaChampionReository;
    
    @Autowired
    private MatchRepository matchRepository;
    
    @AfterEach
    public void afterTest() throws Exception {
    	//테스트 코드에 의해 h2 DB에 생성된 데이터들을 삭제해주는 작업
    	matchRepository.deleteAll();
    	jpaChampionReository.deleteChampEnemysAll();
    	jobRepositoryTestUtils.removeJobExecutions();
    }
    
    @Test
    public void testChampEnemyStaticJob() throws Exception {
    	
    	//given
    	Match match1 = new Match();
    	match1.setSeason(seasonId);
    	match1.setMatchId("600");
    	match1.setQueueId(420);
    	match1.setGameEndTimestamp(System.currentTimeMillis()-300);
    	for(int i=0;i<10;i++) {
    		match1.getMembers().add(new Member());
    	}
    	for(int i=0;i<10;i++) {
    		match1.getMembers().get(i).setCk(new MemberCompKey(i+"400", match1.getMatchId()));
    		match1.getMembers().get(i).setMatch(match1);
    		if(i<5) 
    			match1.getMembers().get(i).setWins(true);
    		else 
    			match1.getMembers().get(i).setWins(false);
    	}
    	match1.getMembers().get(0).setChampionid("탈론");  		
    	match1.getMembers().get(1).setChampionid("그레이브즈");
    	match1.getMembers().get(2).setChampionid("카타리나");
    	match1.getMembers().get(3).setChampionid("케이틀린");
    	match1.getMembers().get(4).setChampionid("나미");
    	match1.getMembers().get(5).setChampionid("오른");
    	match1.getMembers().get(6).setChampionid("니달리");
    	match1.getMembers().get(7).setChampionid("제드");
    	match1.getMembers().get(8).setChampionid("징크스");
    	match1.getMembers().get(9).setChampionid("알리스타");
    	
    	match1.getMembers().get(0).setPositions("TOP");
    	match1.getMembers().get(1).setPositions("JUNGLE");
    	match1.getMembers().get(2).setPositions("MIDDLE");
    	match1.getMembers().get(3).setPositions("BOTTOM");
    	match1.getMembers().get(4).setPositions("UTILITY");
    	match1.getMembers().get(5).setPositions("TOP");
    	match1.getMembers().get(6).setPositions("JUNGLE");
    	match1.getMembers().get(7).setPositions("MIDDLE");
    	match1.getMembers().get(8).setPositions("BOTTOM");
    	match1.getMembers().get(9).setPositions("UTILITY");
    	
    	Match match2 = new Match();
    	match2.setSeason(seasonId);
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
    	members2.get(6).setChampionid("카직스");
    	members2.get(7).setChampionid("오리아나");
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
    	
    	List<ChampEnemy> champEnemys1 = jpaChampionReository.findChampEnemys("탈론"); //오른과 전적 2판 다 이김
    	assertThat(champEnemys1.size()).isEqualTo(1);
    	assertThat(champEnemys1.get(0).getCk().getChampionId()).isEqualTo("탈론");
    	assertThat(champEnemys1.get(0).getCk().getEnemychampionId()).isEqualTo("오른");
    	assertThat(champEnemys1.get(0).getWins()).isEqualTo(2);
    	assertThat(champEnemys1.get(0).getLosses()).isEqualTo(0);
    }
}
