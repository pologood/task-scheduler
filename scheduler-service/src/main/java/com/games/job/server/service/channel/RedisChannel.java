package com.games.job.server.service.channel;

import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/27 11:08
 * @project:job-center
 * @full_name:com.games.job.server.service.channel.RedisChannel
 * @ide:IntelliJ IDEA
 */
public class RedisChannel implements Channel{
    private StringRedisTemplate template;

    private String  jobChannel;
    private String  jobStatusChannel;

    @Override
    public Set<TaskModel> getTask() {
        return getTasks(jobChannel);
    }

    @Override
    public Set<TaskModel> getTaskStatus() {
        return getTasks(jobStatusChannel);
    }

    @Override
    public void putTask(TaskModel task) {
        // TODO: 2017/3/1 task校验 
        SetOperations<String,String> ops =  this.template.opsForSet();
        ops.add(task.getTaskGroup(), JsonUtils.toJson(task));
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

    public StringRedisTemplate getTemplate() {
        return template;
    }

    public void setTemplate(StringRedisTemplate template) {
        this.template = template;
    }

    public String getJobChannel() {
        return jobChannel;
    }

    public void setJobChannel(String jobChannel) {
        this.jobChannel = jobChannel;
    }

    public String getJobStatusChannel() {
        return jobStatusChannel;
    }

    public void setJobStatusChannel(String jobStatusChannel) {
        this.jobStatusChannel = jobStatusChannel;
    }
}
