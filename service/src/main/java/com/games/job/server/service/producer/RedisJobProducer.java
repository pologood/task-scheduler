package com.games.job.server.service.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;


@Component
public class RedisJobProducer implements JobProducer {

    @Autowired
    private StringRedisTemplate template;

    @Value("${spring.redis.jobChannel}")
    private String  jobChannel = "";

    @Value("${spring.redis.jobStatusChannel}")
    private String  jobStatusChannel = "";

    /**
     * 时间到时进行任务发送
     * @param task
     */
    @Override
    public void  notifyTaskInstance(TaskModel task){
        SetOperations<String,String> ops =  this.template.opsForSet();
        ops.add(task.getTaskGroup(), JsonUtils.toJson(task));
    }

}
