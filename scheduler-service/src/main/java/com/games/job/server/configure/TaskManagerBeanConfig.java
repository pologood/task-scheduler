package com.games.job.server.configure;

import com.games.job.server.service.TaskManager;
import com.games.job.server.service.channel.Channel;
import com.games.job.server.service.channel.RedisChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author:liujh
 * @create_time:2017/2/27 11:39
 * @project:job-center
 * @full_name:com.games.job.server.configure.TaskManagerConfig
 * @ide:IntelliJ IDEA
 */
@Configuration
public class TaskManagerBeanConfig {

    @Autowired
    private StringRedisTemplate template;

    @Value("${spring.redis.jobChannel}")
    private String  jobChannel;

    @Value("${spring.redis.jobStatusChannel}")
    private String  jobStatusChannel;

    @Bean(name = "taskManager")
    TaskManager taskManager(){
        TaskManager taskManager = new TaskManager();
        taskManager.setChannel(channel());
        return taskManager;
    }

    @Bean
    Channel channel(){
        RedisChannel channel = new RedisChannel();
        channel.setTemplate(template);
        channel.setJobChannel(jobChannel);
        channel.setJobStatusChannel(jobStatusChannel);
        return channel;
    }

}
