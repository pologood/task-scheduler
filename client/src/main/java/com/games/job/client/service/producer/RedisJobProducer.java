package com.games.job.client.service.producer;

import com.games.job.client.service.channel.RedisChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.games.job.common.model.TaskModel;

@Component
public class RedisJobProducer extends JobProducer {

    private static final Logger log = LoggerFactory.getLogger(RedisJobProducer.class);

    @Autowired
    private RedisChannel quartzChannel;

    @Override
    public void sendJob(TaskModel taskModel) {
        quartzChannel.sendJobToChannel(taskModel);
    }

}
