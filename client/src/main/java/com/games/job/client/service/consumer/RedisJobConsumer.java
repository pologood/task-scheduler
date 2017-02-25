package com.games.job.client.service.consumer;

import com.games.job.client.service.JobAgent;
import com.games.job.client.service.channel.Channel;
import com.games.job.client.service.channel.RedisChannel;
import com.games.job.common.constant.Constants;

/**
 * @author:liujh
 * @create_time:2017/2/17 16:27
 * @project:job-center
 * @full_name:com.games.job.client.service.DefaultJobProcessor
 * @ide:IntelliJ IDEA
 */
public class RedisJobConsumer extends JobAgent {
    private RedisChannel redisChannel;

    @Override
    Channel getChannel() {
        return redisChannel;
    }
    @Override
    String getTaskGroup() {
        return Constants.TASK_GROUP_NAME;
    }
}
