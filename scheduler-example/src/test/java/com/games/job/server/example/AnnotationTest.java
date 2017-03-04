package com.games.job.server.example;

import com.games.job.client.annotation.Quartz;
import com.games.job.client.job.Job;
import org.springframework.stereotype.Service;


@Service
@Quartz(jobName = "AnnotationTest",cronExpression = "0 0/1 * * * ?",retryCount = 3)
public class AnnotationTest implements Job {
    @Override
    public void run() {
        throw  new RuntimeException("runtimeException");
    }
}
