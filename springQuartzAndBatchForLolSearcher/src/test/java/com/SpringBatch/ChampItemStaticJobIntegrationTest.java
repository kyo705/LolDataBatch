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

import com.SpringBatch.Entity.Champion.ChampionItem.ChampItem;
import com.SpringBatch.Entity.match.Match;
import com.SpringBatch.Entity.match.Member;
import com.SpringBatch.Entity.match.MemberCompKey;
import com.SpringBatch.Jobs.ChampItemStatic.ChampItemStataicBatchConfig;
import com.SpringBatch.repository.MatchRepository;
import com.SpringBatch.repository.championrepository.ChampionReository;
import com.SpringBatch.repository.championrepository.JpaChampionRepository;

@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes= {ChampItemStataicBatchConfig.class, TestBatchConfig.class, JpaChampionRepository.class})
public class ChampItemStaticJobIntegrationTest {
	
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
    	//�׽�Ʈ �ڵ忡 ���� h2 DB�� ������ �����͵��� �������ִ� �۾�
    	matchRepository.deleteAll();
    	jpaChampionReository.deleteChampItemsAll();
    	jobRepositoryTestUtils.removeJobExecutions();
    }
    
    @Test
    public void testChampItemStaticJob() throws Exception {
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
    	//�ϳ��� ��ġ�� 10���� è�Ǿ� ����
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
    	//è�Ǿ� �� ������ ����
    	match1.getMembers().get(0).setItem0(30);
    	match1.getMembers().get(0).setItem1(31);
    	match1.getMembers().get(0).setItem2(32);
    	match1.getMembers().get(1).setItem0(60);
    	match1.getMembers().get(1).setItem3(30);
    	match1.getMembers().get(2).setItem3(30);
    	match1.getMembers().get(3).setItem3(30);
    	match1.getMembers().get(4).setItem3(30);
    	match1.getMembers().get(5).setItem3(30);
    	match1.getMembers().get(6).setItem3(30);
    	match1.getMembers().get(7).setItem3(30);
    	match1.getMembers().get(8).setItem3(30);
    	match1.getMembers().get(9).setItem3(30);
    	
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
    	//è�Ǿ� �� ������ ����
    	match1.getMembers().get(0).setItem3(30);
    	match1.getMembers().get(0).setItem4(31);
    	match1.getMembers().get(0).setItem5(33);
    	match1.getMembers().get(1).setItem0(60);
    	match1.getMembers().get(1).setItem3(30);
    	match1.getMembers().get(2).setItem0(60);
    	match1.getMembers().get(2).setItem1(70);
    	match1.getMembers().get(2).setItem2(66);
    	match1.getMembers().get(3).setItem0(60);
    	match1.getMembers().get(3).setItem1(32);
    	match1.getMembers().get(4).setItem0(50);
    	match1.getMembers().get(5).setItem0(50);
    	match1.getMembers().get(6).setItem0(50);
    	match1.getMembers().get(7).setItem0(50);
    	match1.getMembers().get(8).setItem0(50);
    	match1.getMembers().get(9).setItem0(50);
    	
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
    	
    	List<ChampItem> champItems = jpaChampionReository.findChampItems("�׷��̺���");
    	assertThat(champItems.size()).isEqualTo(2);
    	for(ChampItem champItem : champItems) {
    		System.out.println(champItem.getCk().getChampionId()+" "+champItem.getCk().getItemId()
    				+" "+champItem.getWins() +"/"+champItem.getLosses());
    	}
    	
    }
}
