package com.games.job.server.service;

import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.games.job.server.ApplicationTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.Set;


public class TaskManagerTest extends ApplicationTest{

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TaskManager taskManager;


    @Value("${spring.redis.jobChannel}")
    private String  jobChannel;

    @Value("${spring.redis.jobStatusChannel}")
    private String  jobStatusChannel;

    @Value("${spring.quartz.group}")
    private String group;

    // TODO: 2017/3/3 1.mq channel测试；2.restful channel测试；3.c-s联合测试；4.example完善；5.压力测试；6.admin控制台。
    // TODO: 2017/3/3 不清楚的地方搞清楚，1.springboot如何处理.yml文件，一些注解含义，序列化问题。

    @Test
    public void test_sentTaskToRedis(){
        String group = "notify";
        redisTemplate.delete(group);
        for(int i=0;i<2;i++){
            TaskModel taskModel = new TaskModel();
            taskModel.setJobGroup(group);
            taskModel.setBeginTime(new Date());
            taskModel.setTaskId(i);
            taskModel.setBeanName("bean"+i);
            taskModel.setJobName("demo"+i);
            taskManager.sendTask(taskModel);
        }
        Assert.assertTrue(redisTemplate.opsForSet().size(group)==2);
    }

    @Test
    public void test_receiveTaskFromRedis(){
        redisTemplate.delete(jobChannel);
        SetOperations<String,String> redisOptions = redisTemplate.opsForSet();
        for(int i=0;i<3;i++){
            TaskModel taskModel = new TaskModel();
            taskModel.setJobName("testJob"+i);
            taskModel.setBeanName("bean"+i);
            taskModel.setTaskId(i);
            taskModel.setCreateTime(new Date());
            taskModel.setCronExpression("0 0/1 * * * ?");
            taskModel.setJobGroup(group);
            taskModel.setStatus(TaskStatus.INIT.getId());
            redisOptions.add(jobChannel, JsonUtils.toJson(taskModel));
        }
        Set<TaskModel> taskModels = taskManager.receiveTask();
        Assert.assertTrue(taskModels.size()==3);
    }

    @Test
    public void test_receiveTaskStatusFromRedis(){
        redisTemplate.delete(jobStatusChannel);
        SetOperations<String,String> redisOptions = redisTemplate.opsForSet();
        for(int i=0;i<3;i++){
            TaskModel taskModel = new TaskModel();
            taskModel.setJobName("testJob"+i);
            taskModel.setBeanName("bean"+i);
            taskModel.setTaskId(i);
            taskModel.setBeginTime(new Date());
            taskModel.setCronExpression("0 0/1 * * * ?");
            taskModel.setJobGroup(group);
            taskModel.setStatus(TaskStatus.BEGIN.getId());
            redisOptions.add(jobStatusChannel, JsonUtils.toJson(taskModel));
        }
        Set<TaskModel> taskModels = taskManager.receiveTaskStatus();
        Assert.assertTrue(taskModels.size()==3);
    }
}
