package com.games.job.client.service.consumer;

import com.games.job.client.service.JobAgent;
import com.games.job.client.service.channel.MqChannel;
import com.games.job.common.constant.Constants;

import com.games.job.client.service.channel.Channel;

/**
 * @author:liujh
 * @create_time:2017/2/17 16:27
 * @project:job-center
 * @full_name:com.games.job.client.service.DefaultJobProcessor
 * @ide:IntelliJ IDEA
 */
public class MqJobConsumer extends JobAgent {
    private MqChannel mqChannel;

    @Override
    Channel getChannel() {
        return mqChannel;
    }
    @Override
    String getTaskGroup() {
        return Constants.TASK_GROUP_NAME;
    }

}
