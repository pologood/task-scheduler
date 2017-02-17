package com.games.job.server.task;

import java.util.Set;

import com.games.job.common.model.TaskModel;
import com.games.job.server.service.TaskService;
import com.games.job.server.service.consumer.RedisJobConsumer;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskChannelJob implements Job {
    /**
     * 从redis中发现task
     */
    @Autowired
    private RedisJobConsumer redisJobConsumer;

    @Autowired
    private TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskChannelJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("TaskChannelJob  start ");
        Set<TaskModel> set = redisJobConsumer.getTaskFromChannel();
        if(!set.isEmpty()){
            taskService.initJobs(set);
        }
        logger.info("TaskChannelJob  end ");

    }
}
