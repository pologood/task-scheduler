package com.games.job.server.demo.tesk;

import com.games.job.client.annotation.Quartz;
import com.games.job.client.task.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("scheduleDemo")
@Quartz(jobName = "jobNameTest",cronExpression = "0 0/1 * * * ?",retryCount = 4)
public class ScheduleDemo implements Job {

    private static final Logger log = LoggerFactory.getLogger(ScheduleDemo.class);

    public void  run(){
        log.error("报错就对了");
    }
}
