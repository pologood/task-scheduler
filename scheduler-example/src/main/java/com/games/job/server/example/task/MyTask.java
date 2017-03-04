package com.games.job.server.example.task;

import com.games.job.client.annotation.Quartz;
import com.games.job.client.job.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@Quartz(jobName = "myJob",cronExpression = "0 0/1 * * * ?",retryCount = 4)
public class MyTask implements Job {

    private static final Logger log = LoggerFactory.getLogger(MyTask.class);

    public void  run(){
        log.error("报错就对了");
    }
}
