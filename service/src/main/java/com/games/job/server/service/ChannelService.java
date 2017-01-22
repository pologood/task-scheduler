package com.games.job.server.service;

import java.util.HashSet;
import java.util.Set;

import com.games.job.server.model.TaskModel;
import com.games.job.server.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;



@Component
public class ChannelService {

    @Autowired
    private StringRedisTemplate template;

    @Value("${spring.redis.jobChannel}")
    private String  jobChannel = "";

    @Value("${spring.redis.jobStatusChannel}")
    private String  jobStatusChannel = "";

    public void  notifyTaskInstance(TaskModel task){
        SetOperations<String,String> ops =  this.template.opsForSet();
        ops.add(task.getTaskGroup(), JsonUtils.toJson(task));
    }
    public Set<TaskModel>  getTaskFromMachineChannel(){

        return getTasks(jobStatusChannel);
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

    public Set<TaskModel> getTaskFromChannel(){

        return getTasks(jobChannel);
    }

}
