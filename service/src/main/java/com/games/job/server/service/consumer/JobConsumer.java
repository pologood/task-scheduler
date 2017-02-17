package com.games.job.server.service.consumer;

import java.util.Set;

import com.games.job.common.model.TaskModel;


public interface JobConsumer {

    /**
     * 得到所有的任务以便更新状态
     * @return
     */
    Set<TaskModel>  getTaskFromMachineChannel();


    Set<TaskModel> getTaskFromChannel();

}
