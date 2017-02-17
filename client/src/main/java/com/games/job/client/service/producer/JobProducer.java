package com.games.job.client.service.producer;

import com.games.job.client.annotation.Quartz;
import com.games.job.client.task.Job;
import com.games.job.common.model.TaskModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author:liujh
 * @create_time:2017/2/17 18:13
 * @project:job-center
 * @full_name:com.games.job.client.service.reporter.ReporterAdapter
 * @ide:IntelliJ IDEA
 */
@Component
public abstract class JobProducer implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    private static final Logger log = LoggerFactory.getLogger(JobProducer.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        initTask();
        log.info("TaskInitBean has  init");
    }

    public void initTask() {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(Quartz.class);
        for (Map.Entry<String, Object> t : map.entrySet()) {
            Object object = t.getValue();
            if(!isImplJob(object.getClass())){
                continue;
            }
            Quartz quartz = object.getClass().getDeclaredAnnotation(Quartz.class);
            TaskModel taskModel = new TaskModel();
            taskModel.setTaskGroup(quartz.groupName());
            taskModel.setJobName(quartz.jobName());
            taskModel.setCronExpression(quartz.cronExpression());
            taskModel.setRetryCount(quartz.retryCount());
            taskModel.setBeanName(t.getKey());
            sendJob(taskModel);
        }
    }

    private boolean isImplJob(Class c){
        Class[]  classes = c.getInterfaces();
        if(classes.length==0){
            return false;
        }
        for(Class c1 :classes){
            if(c1.equals(Job.class)){
                return true;
            }
        }
        return false;
    }

    /**
     * 发送注册信息到redis
     * @param taskModel
     */
    public abstract   void   sendJob(TaskModel taskModel);

}
