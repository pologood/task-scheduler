package com.games.job.client.service.consumer;

import com.games.job.client.service.channel.MqChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.games.job.client.service.channel.Channel;

/**
 * @author:liujh
 * @create_time:2017/2/17 16:27
 * @project:job-center
 * @full_name:com.games.job.client.service.DefaultJobProcessor
 * @ide:IntelliJ IDEA
 */
@Component
public abstract class MqJobConsumer extends JobConsumer {
    @Autowired
    private MqChannel mqChannel;

    @Override
    Channel getChannel() {
        return mqChannel;
    }
}
