package com.games.job.server.service.producer;


import com.games.job.common.constant.Constants;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.games.job.common.model.TaskModel;


@Component
public class MqJobProducer implements JobProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    /**
     * 时间到时进行任务发送
     * @param task
     */
    @Override
    public void  notifyTaskInstance(TaskModel task){
        kafkaTemplate.send(task.getTaskGroup(),task);
    }

}
