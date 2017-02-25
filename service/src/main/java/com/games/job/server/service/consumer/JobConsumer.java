package com.games.job.server.service.consumer;

import java.util.Set;

import com.games.job.common.model.TaskModel;


public interface JobConsumer {

    /**
     * 获取任务状态更新
     * @return
     */
    Set<TaskModel>  getTaskFromStatusChannel();


    /**
     * 获取任务入库
     * @return
     */
    Set<TaskModel> getTaskFromChannel();

}
