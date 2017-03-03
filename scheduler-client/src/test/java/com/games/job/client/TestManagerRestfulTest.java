package com.games.job.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.games.job.client.service.TaskManager;

/**
 * @author:liujh
 * @create_time:2017/3/1 16:25
 * @project:task-scheduler
 * @full_name:com.games.job.client.Test
 * @ide:IntelliJ IDEA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@TestPropertySource(locations = {"classpath:test.yml"})
public class TestManagerRestfulTest {

    @Autowired
    private TaskManager taskManager;

    @Test
    public void test_sendTaskToMqByInit(){

    }
}
