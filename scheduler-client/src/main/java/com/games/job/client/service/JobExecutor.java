package com.games.job.client.service;

import com.games.job.common.model.TaskModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author:liujh
 * @create_time:2017/3/1 10:56
 * @project:job-center
 * @full_name:com.games.job.client.service.ExeJob
 * @ide:IntelliJ IDEA
 */
@Component
public class JobExecutor implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(JobExecutor.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TaskManager taskManager;

    private int threadCount=5;

    @Value("${spring.quartz.group}")
    private String quartzGroup;

    private ExecutorService executorService;

    public JobExecutor(){
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    @Override
    public void run() {
        log.info("ExeJob  run");
        try {
            Set<TaskModel> taskModes = taskManager.receiveTasks(quartzGroup);
            for (TaskModel taskModel : taskModes) {
                // TODO: 2016/11/21 此处必须异步执行
                executorService.execute(new JobHandler(taskModel,applicationContext,taskManager));
            }
        } catch (Exception e) {
            log.error("JobProcessor@Schedule@run -  Schedule run error ", e);
        }
    }
}
