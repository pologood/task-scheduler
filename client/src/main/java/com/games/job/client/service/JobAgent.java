package com.games.job.client.service;

import com.games.job.client.job.Job;
import com.games.job.common.constant.Constants;
import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class JobAgent implements InitializingBean {

    private ExecutorService executorService;

    private ScheduledExecutorService scheduledExecutorService ;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JobRunner jobRunner;
    @Autowired
    private TaskReporter taskReporter;

    @Value("${spring.quartz.threadCount}")
    private int threadCount = 5;

    private static final Logger log = LoggerFactory.getLogger(JobAgent.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = Executors.newFixedThreadPool(threadCount);
        scheduledExecutorService.scheduleWithFixedDelay(new JobAgent.Schedule(), 100, 200, TimeUnit.SECONDS);
        log.info("JobProcessor has init");
    }

    private class Schedule implements Runnable {
        @Override
        public void run() {
            log.info("JobProcessor@Schedule task  run");
            try {
                Set<TaskModel> taskModes = jobRunner.getNotification();
                for (TaskModel taskModel : taskModes) {
                    // TODO: 2016/11/21 此处必须异步执行
                    executorService.execute(new JobAgent.Poller(taskModel));
                }
            } catch (Exception e) {
                log.error("JobProcessor@Schedule@run -  Schedule run error ", e);
            }
        }
    }

    private class Poller implements Runnable {
        private TaskModel taskModel;

        public Poller(TaskModel taskModel) {
            this.taskModel = taskModel;
        }

        @Override
        public void run() {
            try {
                log.info("JobProcessor@Poller task  run");
                process(taskModel);
            } catch (Exception e) {
                log.error("JobProcessor@Poller@run -  poller run error - para:{}", taskModel, e);
            }
        }
    }

    public void process(TaskModel taskModel) {
        Job job = (Job) applicationContext.getBean(taskModel.getBeanName());
        if(job==null){
            log.error("@process - cannot find  bean  by name :{}", taskModel.getBeanName());
        }
        taskReporter.sendBeginStatus(taskModel);
        try {
            job.run();
            taskReporter.sendEndStatus(taskModel);
        } catch (Exception e) {
            log.error("@process - job  invoke fail - para:{}", taskModel,e);
            taskReporter.sendFailStatus(taskModel);
        }

    }


}
