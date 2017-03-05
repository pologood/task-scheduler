package com.games.job.client.service;

import com.games.job.client.job.Job;
import com.games.job.common.model.TaskModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author:liujh
 * @create_time:2017/3/1 11:13
 * @project:job-center
 * @full_name:com.games.job.client.service.JobHandle
 * @ide:IntelliJ IDEA
 */
public class JobHandler implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(JobHandler.class);

    private TaskModel taskModel;
    private ApplicationContext applicationContext;
    private TaskManager taskManager;

    public JobHandler(TaskModel taskModel, ApplicationContext applicationContext, TaskManager taskManager) {
        this.taskModel = taskModel;
        this.applicationContext=applicationContext;
        this.taskManager =taskManager;
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


    private void process(TaskModel taskModel) {
        Job job = (Job) applicationContext.getBean(taskModel.getBeanName());
        if(job==null){
            log.error("@process - cannot find  bean  by name :{}", taskModel.getBeanName());
        }
        taskManager.sendBeginStatus(taskModel);
        try {
            job.run();
            taskManager.sendEndStatus(taskModel);
        } catch (Exception e) {
            log.error("@process - job  invoke fail - para:{}", taskModel,e);
//            StringWriter sw = new StringWriter();
//            e.printStackTrace(new PrintWriter(sw));
//            String trace = sw.toString();
            taskModel.setFailReason(ExceptionUtils.getStackTrace(e));
            taskManager.sendFailStatus(taskModel);
        }
    }
}
