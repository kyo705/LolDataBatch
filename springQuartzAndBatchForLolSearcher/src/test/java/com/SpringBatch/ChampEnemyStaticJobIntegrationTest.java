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
import com.SpringBatch.repository.ChampEnemyRepository;
import com.SpringBatch.repository.MatchRepository;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes= {ChampEnemyStaticBatchConfig.class, TestBatchConfig.class})
public class ChampEnemyStaticJobIntegrationTest {

	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    
    @Autowired
    private ChampEnemyRepository champEnemyRepository;
    
    @Autowired
    private MatchRepository matchRepository;
    
    @AfterEach
    public void afterTest() throws Exception {
    	//�׽�Ʈ �ڵ忡 ���� h2 DB�� ������ �����͵��� �������ִ� �۾�
    	matchRepository.deleteAll();
    	champEnemyRepository.deleteAll();
    	jobRepositoryTestUtils.removeJobExecutions();
    }
    
    @Test
    public void testChampEnemyStaticJob() throws Exception {
    	
    	//given
    	Match match1 = new Match();
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
    	match1.getMembers().get(0).setChampionid("Ż��");  		
    	match1.getMembers().get(1).setChampionid("�׷��̺���");
    	match1.getMembers().get(2).setChampionid("īŸ����");
    	match1.getMembers().get(3).setChampionid("����Ʋ��");
    	match1.getMembers().get(4).setChampionid("����");
    	match1.getMembers().get(5).setChampionid("����");
    	match1.getMembers().get(6).setChampionid("�ϴ޸�");
    	match1.getMembers().get(7).setChampionid("����");
    	match1.getMembers().get(8).setChampionid("¡ũ��");
    	match1.getMembers().get(9).setChampionid("�˸���Ÿ");
    	
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
    	members2.get(0).setChampionid("Ż��");  		
    	members2.get(1).setChampionid("�׷��̺���");
    	members2.get(2).setChampionid("īŸ����");
    	members2.get(3).setChampionid("����Ʋ��");
    	members2.get(4).setChampionid("����");
    	members2.get(5).setChampionid("����");
    	members2.get(6).setChampionid("ī����");
    	members2.get(7).setChampionid("�����Ƴ�");
    	members2.get(8).setChampionid("¡ũ��");
    	members2.get(9).setChampionid("�˸���Ÿ");
    	
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
    	for(int i=0;i<10;i++) {
    		System.out.println(match1.getMembers().get(i).getChampionid()+" "+match1.getMatchId()+" "+match1.getMembers().get(i).getCk().getSummonerid());
    	}
    	
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
    	
    	List<ChampEnemy> champEnemys = champEnemyRepository.findAll();
    	for(ChampEnemy champEnemy : champEnemys) {
    		System.out.println(champEnemy.getCk().getChampionId() + " " + champEnemy.getCk().getEnemychampionId() + " "
    	+ champEnemy.getWins()+"/"+champEnemy.getLosses());
    	}
    }
}
