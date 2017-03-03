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
    private TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskMonitorJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // TODO: 2017/3/3 cron表达式正确性校验，group+jobName唯一性校验
        logger.info("TaskChannelJob  start ");
        Set<TaskModel> set = taskManager.receiveTask();
        if(!set.isEmpty()){
            taskService.initQuartzs(set);
        }
        logger.info("TaskChannelJob  end ");
    }
}
