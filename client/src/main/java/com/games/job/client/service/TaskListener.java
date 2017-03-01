package com.games.job.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class TaskListener implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(TaskListener.class);

    private ScheduledExecutorService scheduledExecutorService=Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private JobExecutor jobExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduledExecutorService.scheduleWithFixedDelay(jobExecutor, 100, 200, TimeUnit.SECONDS);
        log.info("JobProcessor has init");
    }


}
