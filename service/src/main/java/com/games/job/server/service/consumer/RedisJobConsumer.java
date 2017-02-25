package com.games.job.server.service.consumer;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;


@Component
public class RedisJobConsumer implements JobConsumer {

    @Autowired
    private StringRedisTemplate template;

    @Value("${spring.redis.jobChannel}")
    private String  jobChannel = "";

    @Value("${spring.redis.jobStatusChannel}")
    private String  jobStatusChannel = "";


    @Override
    public Set<TaskModel> getTaskFromStatusChannel(){
        return getTasks(jobStatusChannel);
    }


    @Override
    public Set<TaskModel> getTaskFromChannel(){
        return getTasks(jobChannel);
    }


    private Set<TaskModel> getTasks(String key) {
        SetOperations<String,String> ops =  this.template.opsForSet();
        Set<TaskModel> set = new HashSet();
        while (true) {
            String taskJson = ops.pop(key);
            if (taskJson == null) {
                break;
            }
            set.add(JsonUtils.fromJson(taskJson,TaskModel.class));
        }
        return set;
    }
}
