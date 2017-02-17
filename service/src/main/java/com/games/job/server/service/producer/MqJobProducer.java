package com.games.job.server.service.producer;


import org.springframework.stereotype.Component;

import com.games.job.common.model.TaskModel;


@Component
public class MqJobProducer implements JobProducer {

    /**
     * 时间到时进行任务发送
     * @param task
     */
    @Override
    public void  notifyTaskInstance(TaskModel task){
    }

}
