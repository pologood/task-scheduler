package com.games.job.server.service.consumer;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.games.job.common.model.TaskModel;


@Component
public class MqJobConsumer implements JobConsumer {

    /**
     * 得到所有的任务以便更新状态
     * @return
     */
    @Override
    public Set<TaskModel>  getTaskFromMachineChannel(){
        return null;
    }

    @Override
    public Set<TaskModel> getTaskFromChannel(){
        return null;
    }

}
