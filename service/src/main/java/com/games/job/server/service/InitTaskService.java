package com.games.job.server.service;

import java.util.Set;

import com.games.job.server.model.TaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InitTaskService {

    @Autowired
    private TaskService taskService;
    /**
     * 根据从交互通道得到的数据初始化或者更新task
     * @param set
     */
    // TODO: 2016/11/16 同时更新task表
    public void initJobs(Set<TaskModel> set ){

        set.stream().forEach(taskModel -> {
            taskService.addOrUpdateJob(taskModel);
        });
    }
}