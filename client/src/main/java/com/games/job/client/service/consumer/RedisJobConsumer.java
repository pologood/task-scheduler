package com.games.job.client.service.consumer;

import com.games.job.client.service.channel.Channel;
import com.games.job.client.service.channel.RedisChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author:liujh
 * @create_time:2017/2/17 16:27
 * @project:job-center
 * @full_name:com.games.job.client.service.DefaultJobProcessor
 * @ide:IntelliJ IDEA
 */
@Component
public abstract class RedisJobConsumer extends JobConsumer {
    @Autowired
    private RedisChannel redisChannel;


    @Override
    Channel getChannel() {
        return redisChannel;
    }
}
