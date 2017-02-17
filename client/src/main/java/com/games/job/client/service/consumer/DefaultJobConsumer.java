package com.games.job.client.service.consumer;

import com.games.job.common.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.games.job.client.service.channel.Channel;
import com.games.job.client.service.channel.RedisChannel;

/**
 * @author:liujh
 * @create_time:2017/2/17 16:27
 * @project:job-center
 * @full_name:com.games.job.client.service.DefaultJobProcessor
 * @ide:IntelliJ IDEA
 */
@Component
public class DefaultJobConsumer extends JobConsumer {
    @Autowired
    private RedisChannel redisChannel;

    @Override
    String getTaskGroup() {
        return Constants.TASK_GROUP_NAME;
    }

    @Override
    Channel getChannel() {
        return redisChannel;
    }
}
