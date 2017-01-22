package com.games.job.server.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.games.job.server.model.TaskModel;
import com.games.job.server.service.ChannelService;
import com.games.job.server.service.TaskService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMachineStatusChannelJob implements Job {

    @Autowired
    private ChannelService redisService;

    @Autowired
    private TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskMachineStatusChannelJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("TaskMachineStatusChannelJob start");
        Set<TaskModel> set = redisService.getTaskFromMachineChannel();
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
        taskService.batchUpdateTaskMachineStatus(taskModelList);
        logger.info("TaskMachineStatusChannelJob");
    }
}
