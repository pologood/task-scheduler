package com.games.job.server.job;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class LocalJobInitBean implements InitializingBean {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public void afterPropertiesSet() throws Exception {
        initStatusMonitorJob();
        initTaskChannelListenerJob();
        initTaskMachineStatusChannelListenerJob();
    }

    private void  initTaskMachineStatusChannelListenerJob() throws SchedulerException {
        String jobName = "taskMachineStatusChannelListener";
        String jobGroup = "Quartz";
        String triggerName = "taskStatusChannelListenerTrigger";
        String cronExpression = "0 0/1 * * * ?";
        initScheduler(jobName, jobGroup, triggerName, cronExpression, TaskStatusMonitorJob.class);//ok
    }

    private void  initTaskChannelListenerJob() throws SchedulerException {
        String jobName = "taskChannelListener";
        String jobGroup = "Quartz";
        String triggerName = "taskChannelListenerTrigger";
        String cronExpression = "0 0/1 * * * ?";
        initScheduler(jobName, jobGroup, triggerName, cronExpression, TaskMonitorJob.class);//ok
    }



    private void initStatusMonitorJob() throws SchedulerException {
        String jobName = "monitorTaskStatus";
        String jobGroup = "Quartz";
        String triggerName = "taskStatusMonitorTrigger";
        String cronExpression = "0 0/5 * * * ?";
        initScheduler(jobName, jobGroup, triggerName, cronExpression, TaskFeedJob.class);//ok
    }

    private void initScheduler(String jobName, String jobGroup, String triggerName, String cronExpression,Class jobClass) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetail jobDetail = newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .build();
        CronTrigger trigger = newTrigger()
                .withIdentity(triggerName, jobGroup)
                .startNow()
                .withSchedule(cronSchedule(cronExpression))
                .build();
        if (scheduler.checkExists(jobDetail.getKey())) {
            scheduler.rescheduleJob(trigger.getKey(), trigger);
        } else {
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }
}
