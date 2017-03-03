package com.games.job.client;

import com.games.job.client.service.TaskManager;
import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
public class TestManagerRedisTest {
    @Autowired
    private ShardedJedisPool shardedJedisPool;
    @Autowired
    private TaskManager taskManager;

    @Value("${spring.redis.jobChannel}")
    private String  jobChannel;

    @Value("${spring.redis.jobStatusChannel}")
    private String  jobStatusChannel;

    @Test
    public void test_sendTaskToRedisByInit(){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            String result =shardedJedis.srandmember(jobChannel);
            TaskModel taskModel = JsonUtils.fromJson(result, TaskModel.class);
            Assert.assertNotNull(taskModel);
            Assert.assertEquals("myJob",taskModel.getJobName());
        }finally {
            shardedJedis.close();
        }
    }

    @Test
    public void test_sendTaskToRedis(){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        shardedJedis.del(jobChannel);
        List<TaskModel> taskModels = Lists.newArrayList();
        for(int i=0;i<2;i++){
            TaskModel taskModel = new TaskModel();
            taskModel.setTaskGroup("demo");
            taskModel.setJobName("testJob"+i);
            taskModel.setBeanName("bean"+i);
            taskModel.setCronExpression("0 0/1 * * * ?");
            taskModel.setStatus(TaskStatus.INIT.getId());
            taskModel.setRetryCount(i);
            taskModel.setCreateTime(new Date());
            taskModels.add(taskModel);
        }
        taskManager.sendTasks(taskModels);
        Assert.assertTrue(shardedJedis.scard(jobChannel)==2);
    }


    @Test
    public  void test_sendTaskStatusToRedis(){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        shardedJedis.del(jobStatusChannel);
        for(int i=0;i<2;i++){
            TaskModel taskModel = new TaskModel();
            taskModel.setTaskId(i);
            taskModel.setTaskGroup("demo");
            taskModel.setJobName("testJob"+i);
            taskModel.setBeanName("bean"+i);
            taskModel.setCronExpression("0 0/1 * * * ?");
            taskModel.setStatus(TaskStatus.BEGIN.getId());
            taskModel.setRetryCount(i);
            taskModel.setBeginTime(new Date());
            taskManager.sendBeginStatus(taskModel);
        }
        Assert.assertTrue(shardedJedis.scard(jobStatusChannel)==2);
    }

    @Test
    public void test_receiveJobFromRedis(){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        shardedJedis.del(jobChannel);
        List<TaskModel> taskModels = Lists.newArrayList();
        for(int i=0;i<2;i++){
            TaskModel taskModel = new TaskModel();
            taskModel.setTaskGroup(jobChannel);
            taskModel.setJobName("testJob"+i);
            taskModel.setBeanName("bean"+i);
            taskModel.setCronExpression("0 0/1 * * * ?");
            taskModel.setStatus(TaskStatus.BEGIN.getId());
            taskModel.setRetryCount(i);
            taskModel.setBeginTime(new Date());
            taskModels.add(taskModel);
        }
        taskManager.sendTasks(taskModels);
        Set<TaskModel> taskModelSet = taskManager.receiveTasks(jobChannel);
        Assert.assertTrue(taskModelSet.size()==2);
    }

}
