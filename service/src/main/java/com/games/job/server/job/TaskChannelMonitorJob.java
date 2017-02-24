package com.games.job.server.job;

import java.util.Set;

import com.games.job.common.model.TaskModel;
import com.games.job.server.service.JobService;
import com.games.job.server.service.consumer.RedisJobConsumer;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskChannelMonitorJob implements Job {
    /**
     * 从redis中发现task
     */
    @Autowired
    private RedisJobConsumer redisJobConsumer;

    @Autowired
    private JobService jobService;

    private static final Logger logger = LoggerFactory.getLogger(TaskChannelMonitorJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("TaskChannelJob  start ");
        Set<TaskModel> set = redisJobConsumer.getTaskFromChannel();
        if(!set.isEmpty()){
            jobService.initJobs(set);
        }
        logger.info("TaskChannelJob  end ");
    }
}