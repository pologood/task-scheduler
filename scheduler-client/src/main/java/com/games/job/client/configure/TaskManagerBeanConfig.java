package com.games.job.client.configure;

import com.games.job.client.service.TaskManager;
import com.games.job.client.service.channel.Channel;
import com.games.job.client.service.channel.RedisChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author:liujh
 * @create_time:2017/2/27 13:00
 * @project:job-center
 * @full_name:com.games.job.client.configure.TaskManagerBeanConfig
 * @ide:IntelliJ IDEA
 */
@Configuration
public class TaskManagerBeanConfig {

    @Value("${spring.redis.jobChannel}")
    private String  jobChannel;

    @Value("${spring.redis.jobStatusChannel}")
    private String  jobStatusChannel;

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Bean
    public TaskManager taskManager(){
        TaskManager taskManager = new TaskManager();
        taskManager.setChannel(channel());
        return taskManager;
    }


    @Bean
    public Channel channel(){
        RedisChannel redisChannel =new RedisChannel();
        redisChannel.setJobStatusChannel(jobStatusChannel);
        redisChannel.setJobChannel(jobChannel);
        redisChannel.setShardedJedisPool(shardedJedisPool);
        return redisChannel;
    }

}
