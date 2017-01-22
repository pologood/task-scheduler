package com.games.job.demo.service;

import com.games.job.demo.model.TaskModel;
import com.games.job.demo.enums.TaskStatus;
import com.games.job.demo.task.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class JobProcessor implements InitializingBean {

    @Autowired
    private QuartzChannel redisService;

    private ExecutorService executorService;

    @Value("${spring.quartz.threadCount}")
    private int threadCount = 5;

    private ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private ApplicationContext applicationContext;

    private static final Logger log = LoggerFactory.getLogger(JobProcessor.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = Executors.newFixedThreadPool(threadCount);
        schedule.scheduleWithFixedDelay(new Schedule(), 100, 200, TimeUnit.SECONDS);
        log.info("JobProcessor has init");
    }

    private class Schedule implements Runnable {

        @Override
        public void run() {
            log.info("JobProcessor@Schedule task  run");
            try {
                Set<TaskModel> taskModes = redisService.getNotificationFromChannel();
                for (TaskModel taskModel : taskModes) {
                    // TODO: 2016/11/21 此处必须异步执行
                    executorService.execute(new Poller(taskModel));
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
        sendBeginStatus(taskModel);
        try {
            job.run();
            sendEndStatus(taskModel);
        } catch (Exception e) {
            log.error("@process - job  invoke fail - para:{}", taskModel,e);
            sendFailStatus(taskModel);
        }

    }

    private void sendBeginStatus(TaskModel taskModel) {
        TaskModel beginTaskModel = taskModel.clone();
        beginTaskModel.setStatus(TaskStatus.BEGIN.getId());
        beginTaskModel.setBeginTime(new Date());
        beginTaskModel.initDealTime();
        redisService.sendTaskMachineStatusToChannel(beginTaskModel);
    }

    private void sendEndStatus(TaskModel taskModel) {
        TaskModel endTaskModel = taskModel.clone();
        endTaskModel.setEndTime(new Date());
        endTaskModel.setStatus(TaskStatus.END.getId());
        endTaskModel.initDealTime();
        redisService.sendTaskMachineStatusToChannel(endTaskModel);
    }

    private void sendFailStatus(TaskModel taskModel) {
        TaskModel failTaskModel = taskModel.clone();
        failTaskModel.setStatus(TaskStatus.FAIL.getId());
        failTaskModel.setEndTime(new Date());
        failTaskModel.initDealTime();
        redisService.sendTaskMachineStatusToChannel(failTaskModel);
    }
}
