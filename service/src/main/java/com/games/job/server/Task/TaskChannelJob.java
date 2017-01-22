package com.games.job.server.Task;

import java.util.Set;

import com.games.job.server.model.TaskModel;
import com.games.job.server.service.InitTaskService;
import com.games.job.server.service.ChannelService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskChannelJob implements Job {

    @Autowired
    private ChannelService redisService;

    @Autowired
    private InitTaskService initTaskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskChannelJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("TaskChannelJob  start ");
        Set<TaskModel> set = redisService.getTaskFromChannel();
        if(!set.isEmpty()){
            initTaskService.initJobs(set);
        }
        logger.info("TaskChannelJob  end ");

    }
}
