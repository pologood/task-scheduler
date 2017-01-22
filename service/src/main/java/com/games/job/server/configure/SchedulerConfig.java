package com.games.job.server.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix =  ".scheduling")
@Data
public class SchedulerConfig {

    private int threadCount = 20;
    private long clusterCheckInInterval = 1500;
    private long maxMisfiresToHandleAtATime = 1;
    private long misfireThreshold = 120000;
    private boolean disabled = false;
}