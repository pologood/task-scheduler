package com.games.job.server.configure;

import static org.quartz.impl.StdSchedulerFactory.*;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@EnableConfigurationProperties(SchedulerConfig.class)
public class SchedulerBeanConfig {

    @Autowired
    private SchedulerConfig config;

    @Bean
    SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, IrsJobFactory jobFactory) throws Exception {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setStartupDelay(3);
        bean.setOverwriteExistingJobs(true);
        bean.setSchedulerName("IrsScheduler");
        bean.setJobFactory(jobFactory);
        ScheduleLoggingListener loggingListener = new ScheduleLoggingListener();
        bean.setSchedulerListeners(loggingListener);
        bean.setGlobalJobListeners(loggingListener);
        bean.setGlobalTriggerListeners(loggingListener);
        Properties schedulerProps = new Properties();
        schedulerProps.setProperty(PROP_SCHED_INSTANCE_ID, AUTO_GENERATE_INSTANCE_ID);
        schedulerProps.setProperty(PROP_SCHED_THREAD_NAME, "karma.scheduler.worker");
        schedulerProps.setProperty(PROP_SCHED_MAKE_SCHEDULER_THREAD_DAEMON, "true");
        schedulerProps.setProperty(PROP_THREAD_POOL_PREFIX + ".threadCount", String.valueOf(config.getThreadCount()));
        schedulerProps.setProperty(PROP_JOB_STORE_PREFIX + ".isClustered", "true");
        schedulerProps.setProperty(PROP_JOB_STORE_PREFIX + ".useProperties", "true");
        schedulerProps.setProperty(PROP_JOB_STORE_PREFIX + ".clusterCheckinInterval",
                String.valueOf(config.getClusterCheckInInterval()));
        schedulerProps.setProperty(PROP_JOB_STORE_PREFIX + ".maxMisfiresToHandleAtATime",
                String.valueOf(config.getMaxMisfiresToHandleAtATime()));
        schedulerProps
                .setProperty(PROP_JOB_STORE_PREFIX + ".misfireThreshold", String.valueOf(config.getMisfireThreshold()));
        bean.setQuartzProperties(schedulerProps);
        return bean;
    }
}

