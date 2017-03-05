package com.games.job.server.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix =  ".scheduling")
public class SchedulerConfig {

    private int threadCount = 20;
    private long clusterCheckInInterval = 1500;
    private long maxMisfiresToHandleAtATime = 1;
    private long misfireThreshold = 120000;
    private boolean disabled = false;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public long getClusterCheckInInterval() {
        return clusterCheckInInterval;
    }

    public void setClusterCheckInInterval(long clusterCheckInInterval) {
        this.clusterCheckInInterval = clusterCheckInInterval;
    }

    public long getMaxMisfiresToHandleAtATime() {
        return maxMisfiresToHandleAtATime;
    }

    public void setMaxMisfiresToHandleAtATime(long maxMisfiresToHandleAtATime) {
        this.maxMisfiresToHandleAtATime = maxMisfiresToHandleAtATime;
    }

    public long getMisfireThreshold() {
        return misfireThreshold;
    }

    public void setMisfireThreshold(long misfireThreshold) {
        this.misfireThreshold = misfireThreshold;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}