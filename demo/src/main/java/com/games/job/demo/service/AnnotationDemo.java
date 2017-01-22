package com.games.job.demo.service;

import com.games.job.demo.annotation.IrsQuartz;
import com.games.job.demo.task.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("annotationDemo")
@IrsQuartz(jobName = "jobNameTest",cronExpression = "0 0/1 * * * ?",retryCount = 4)
public class AnnotationDemo implements Job {

    private static final Logger log = LoggerFactory.getLogger(AnnotationDemo.class);

    public void  run(){
        log.error("报错就对了");
    }
}
