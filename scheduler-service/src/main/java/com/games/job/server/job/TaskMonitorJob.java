package com.games.job.server.job;

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
public class TaskMonitorJob implements Job {
    /**
     * 从redis中发现task
     */
    @Autowired
    private TaskManager taskManager;

    @Autowired
    private TaskService jobService;

    private static final Logger logger = LoggerFactory.getLogger(TaskMonitorJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("TaskChannelJob  start ");
        Set<TaskModel> set = taskManager.receiveTask();
        if(!set.isEmpty()){
            jobService.initQuartzs(set);
        }
        logger.info("TaskChannelJob  end ");
    }
}
