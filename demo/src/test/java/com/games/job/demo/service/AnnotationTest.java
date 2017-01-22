package com.games.job.demo.service;

import org.springframework.stereotype.Service;

import com.games.job.demo.annotation.IrsQuartz;
import com.games.job.demo.task.Job;

/**
 * Created by wangshichao on 2016/11/23.
 */
@Service("AnnotationTest")
@IrsQuartz(jobName = "AnnotationTest",cronExpression = "0 0/1 * * * ?",retryCount = 3)
public class AnnotationTest implements Job {
    @Override
    public void run() {
        throw  new RuntimeException("runtimeException");
    }
}
