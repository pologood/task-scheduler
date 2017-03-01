package com.games.job.server.demo.tesk;

import java.util.HashSet;
import java.util.Set;

import com.games.job.client.service.consumer.RedisJobConsumer;
import com.games.job.common.utils.JsonUtils;
import com.games.job.server.demo.ApplicationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by wangshichao on 2016/11/23.
 */
public class JobProcesserTest extends ApplicationTest {

    @Autowired
    @Qualifier("redisJobConsumer")
    private RedisJobConsumer redisJobConsumer;

    @Value("${spring.quartz.group}")
    private String group = "";

    @Value("${spring.quartz.jobChannel}")
    private String jobChannel = "";

    @Value("${spring.quartz.jobStatusChannel}")
    private String jobStatusChannel = "";

    @Autowired
    @Qualifier("quartzJedisPool")
    private ShardedJedisPool shardedJedisPool;

    @Test
    public void processTest() {
        TaskModel taskModel = new TaskModel();
        taskModel.setBeanName("annotationDemo");
        taskModel.setStatus(TaskStatus.SEND.getId());
        taskModel.setJobName("jobNameTest");
        taskModel.setTaskGroup(group);
        taskModel.setTaskId(23);
        taskModel.setCronExpression("xxoo");
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        shardedJedis.del(jobStatusChannel);
        redisJobConsumer.process(taskModel);
        Set<String> stringSet = new HashSet();
            while (true) {
                String taskJson = shardedJedis.spop(jobStatusChannel);
                if (taskJson == null) {
                    break;
                }
                stringSet.add(taskJson);
            }
        Assert.assertTrue(stringSet.size() == 2);
        Set<Integer> statusSet = new HashSet<>();
        for (String str : stringSet) {
            TaskModel taskModel1 = JsonUtils.fromJson(str, TaskModel.class);
            taskModel1.validStatusMachineTaskModel();
            if (taskModel1.getStatus().intValue() == TaskStatus.BEGIN.getId()) {
                statusSet.add(taskModel1.getStatus());
                Assert.assertTrue(taskModel1.getBeginTime() != null);
            } else if (taskModel1.getStatus().intValue() == TaskStatus.END.getId()) {
                statusSet.add(taskModel1.getStatus());
                Assert.assertTrue(taskModel1.getEndTime() != null);
            }
        }
        Assert.assertTrue(statusSet.size() == 2);
    }

    @Test
    public void  processJobFailTest(){

        TaskModel taskModel = new TaskModel();
        taskModel.setBeanName("AnnotationTest");
        taskModel.setStatus(TaskStatus.SEND.getId());
        taskModel.setTaskId(23);
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        shardedJedis.del(jobStatusChannel);
        redisJobConsumer.process(taskModel);
        Set<String> stringSet = new HashSet();
        while (true) {
            String taskJson = shardedJedis.spop(jobStatusChannel);
            if (taskJson == null) {
                break;
            }
            stringSet.add(taskJson);
        }
        Assert.assertTrue(stringSet.size() == 2);
        Set<Integer> statusSet = new HashSet<>();
        for (String str : stringSet) {
            TaskModel taskModel1 = JsonUtils.fromJson(str, TaskModel.class);
            taskModel1.validStatusMachineTaskModel();
            if (taskModel1.getStatus().intValue() == TaskStatus.BEGIN.getId()) {
                statusSet.add(taskModel1.getStatus());
                Assert.assertTrue(taskModel1.getBeginTime() != null);
            } else if (taskModel1.getStatus().intValue() == TaskStatus.FAIL.getId()) {
                statusSet.add(taskModel1.getStatus());
                Assert.assertTrue(taskModel1.getEndTime() != null);
            }
        }
        Assert.assertTrue(statusSet.size() == 2);
    }

    @Test
    public void  testSchedule() throws InterruptedException {

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(23);
        taskModel.setBeanName("annotationDemo");
        taskModel.setStatus(TaskStatus.SEND.getId());
        TaskModel taskModel1 = new TaskModel();
        taskModel1.setTaskId(24);
        taskModel1.setBeanName("AnnotationTest");
        taskModel1.setStatus(TaskStatus.SEND.getId());
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        shardedJedis.del(group);
        shardedJedis.sadd(group, JsonUtils.toJson(taskModel));
        shardedJedis.sadd(group, JsonUtils.toJson(taskModel1));
        shardedJedis.del(jobStatusChannel);
        Thread.sleep(20*1000l);
        Set<String> stringSet = new HashSet();
        while (true) {
            String taskJson = shardedJedis.spop(jobStatusChannel);
            if (taskJson == null) {
                break;
            }
            stringSet.add(taskJson);
        }
        Assert.assertTrue(stringSet.size()==4);
    }

}
