package com.games.job.server.demo.tesk;

import com.games.job.client.annotation.Quartz;
import com.games.job.client.task.Job;
import org.springframework.stereotype.Service;


/**
 * Created by wangshichao on 2016/11/23.
 */
@Service("AnnotationTest")
@Quartz(jobName = "AnnotationTest",cronExpression = "0 0/1 * * * ?",retryCount = 3)
public class AnnotationTest implements Job {
    @Override
    public void run() {
        throw  new RuntimeException("runtimeException");
    }
}
