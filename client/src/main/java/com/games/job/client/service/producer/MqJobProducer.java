package com.games.job.client.service.producer;

import com.games.job.client.service.channel.MqChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.games.job.common.model.TaskModel;

@Component
public class MqJobProducer extends JobProducer {

    private static final Logger log = LoggerFactory.getLogger(MqJobProducer.class);

    @Autowired
    private MqChannel mqChannel;

    @Override
    public void sendJob(TaskModel taskModel) {
        mqChannel.sendJobToChannel(taskModel);
    }
}
