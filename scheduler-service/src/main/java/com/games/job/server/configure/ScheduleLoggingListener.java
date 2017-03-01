package com.games.job.server.configure;


import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleLoggingListener implements SchedulerListener, JobListener, TriggerListener {

    private static final Logger log = LoggerFactory.getLogger(ScheduleLoggingListener.class);

    @Override
    public void jobScheduled(Trigger trigger) {

        log.debug("jobScheduled: {}", trigger);
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        log.debug("jobUnScheduled: {}", triggerKey);

    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        log.debug("triggerFinalized: {}", trigger);
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        log.debug("triggerPaused: {}", triggerKey);
    }

    @Override
    public void triggersPaused(String triggerGroup) {
        log.debug("triggersPaused: group = {}", triggerGroup);
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        log.debug("triggerPaused: {}", triggerKey);
    }

    @Override
    public void triggersResumed(String triggerGroup) {
        log.debug("triggersResumed: {}", triggerGroup);
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
        log.debug("jobAdded: {}", jobDetail);
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        log.debug("jobDeleted: {}", jobKey);
    }

    @Override
    public void jobPaused(JobKey jobKey) {
        log.debug("jobPaused: {}", jobKey);
    }

    @Override
    public void jobsPaused(String jobGroup) {
        log.debug("jobsPaused: group = {}", jobGroup);
    }

    @Override
    public void jobResumed(JobKey jobKey) {
        log.debug("jobResumed: {}", jobKey);
    }

    @Override
    public void jobsResumed(String jobGroup) {
        log.debug("jobsResumed: group = {}", jobGroup);
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        log.error("schedulerError: {}", msg, cause);
    }

    @Override
    public void schedulerInStandbyMode() {
        log.info("schedulerInStandbyMode");
    }

    @Override
    public void schedulerStarted() {
        log.info("scheduler started.");
    }

    @Override
    public void schedulerStarting() {
        log.info("scheduler starting.");
    }

    @Override
    public void schedulerShutdown() {
        log.info("scheduler shutdown.");
    }

    @Override
    public void schedulerShuttingdown() {
        log.info("scheduler shutting down.");
    }

    @Override
    public void schedulingDataCleared() {
        log.info("scheduler data cleared.");
    }

    @Override
    public String getName() {
        return getClass().getName() + ".Logging";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.debug("Trigger Fired: {}, context: {}", trigger, context);
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        log.debug("Trigger Misfired: {}", trigger);
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
                                Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        log.debug("Trigger Complete: {}, context: {}", trigger, context);
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.debug("Job to be executed: {}", context);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.debug("Job voted: {}", context);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.debug("Job was executed: {}", context);
        if (null != jobException) {
            log.error("Job execution fail.", jobException);
        }
    }
}

