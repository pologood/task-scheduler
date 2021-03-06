package com.games.job.server.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.games.job.common.model.TaskModel;
import com.games.job.server.service.TaskService;
import com.games.job.server.service.TaskManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskStatusMonitorJob implements Job {
    /**
     * 更新任务状态
     */
    @Autowired
    private TaskManager taskManager;

    @Autowired
    private TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskStatusMonitorJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("TaskMachineStatusChannelJob start");
        Set<TaskModel> set = taskManager.receiveTaskStatus();
        if(set.isEmpty()){
            return;
        }
        List<TaskModel> taskModelList = new ArrayList<>();
        taskModelList.addAll(set);
        taskModelList.sort((TaskModel a, TaskModel b) -> {
            if(a.getDealTime()-b.getDealTime()>0){
                return 1;
            }else if(a.getDealTime()-b.getDealTime()<0){
                return -1;
            }
            return 0;
        });
        taskService.modTasksStatus(taskModelList);
        logger.info("TaskMachineStatusChannelJob");
    }
}
