package com.SpringBatch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.SpringBatch.Jobs.puuids.PuuidsBatchConfig;

@SpringBatchTest
@SpringBootTest(classes= {PuuidsBatchConfig.class, TestBatchConfig.class})
public class PuuidsJobIntegrationTest {

	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    
    @AfterEach
    public void afterTest() throws Exception {
    	//테스트 코드에 의해 h2 DB에 생성된 데이터들을 삭제해주는 작업
    	
    	jobRepositoryTestUtils.removeJobExecutions();
    }
    
    @Test
    public void testPuuidsJob() {
    	
    }
}
