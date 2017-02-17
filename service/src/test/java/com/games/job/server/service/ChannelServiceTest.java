package com.games.job.server.service;

import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.server.ApplicationTest;
import com.games.job.common.utils.JsonUtils;
import com.games.job.server.service.consumer.RedisJobConsumer;
import com.games.job.server.service.producer.RedisJobProducer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.Set;

public class ChannelServiceTest extends ApplicationTest {

    @Autowired
    private RedisJobProducer redisJobProducer;

    @Autowired
    private RedisJobConsumer redisJobConsumer;

    @Value("${spring.redis.jobChannel}")
    private String  jobChannel = "";

    @Value("${spring.redis.jobStatusChannel}")
    private String  jobStatusChannel = "";

    @Autowired
    private StringRedisTemplate template;

    @Test
    public void   notifyTaskInstanceTest(){

        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskStatus.SEND.getId());
        taskModel.setCreateTime(new Date());
        taskModel.setTaskGroup("testGroup");
        this.template.delete(taskModel.getTaskGroup());
        redisJobProducer.notifyTaskInstance(taskModel);
        SetOperations<String,String> ops =  this.template.opsForSet();
        String value = ops.pop(taskModel.getTaskGroup());
        TaskModel taskModel1 = JsonUtils.fromJson(value,TaskModel.class);
        Assert.assertTrue(taskModel1.getStatus().intValue()==TaskStatus.SEND.getId());
    }

    @Test
   public  void getTaskFromChannelTest(){

        this.template.delete(jobChannel);
       TaskModel taskModel = new TaskModel();
       taskModel.setStatus(TaskStatus.INIT.getId());
       taskModel.setCreateTime(new Date());
       taskModel.setTaskGroup(jobChannel);
        redisJobProducer.notifyTaskInstance(taskModel);
       TaskModel taskModel1 = new TaskModel();
       taskModel1.setStatus(TaskStatus.INIT.getId());
       taskModel1.setCreateTime(new Date());
       taskModel.setJobName("testJob2");
       taskModel1.setTaskGroup(jobChannel);
        redisJobProducer.notifyTaskInstance(taskModel1);

       Set<TaskModel>  taskModels =  redisJobConsumer.getTaskFromChannel();
       Assert.assertTrue(taskModels.size()==2);
   }
}
