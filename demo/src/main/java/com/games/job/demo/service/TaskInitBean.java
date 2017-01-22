package com.games.job.demo.service;

import com.games.job.demo.annotation.IrsQuartz;
import com.games.job.demo.model.TaskModel;
import com.games.job.demo.task.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class TaskInitBean implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.quartz.group}")
    private String group = "";

    private static final Logger log = LoggerFactory.getLogger(InitializingBean.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        initTask();
        log.info("TaskInitBean has  init");
    }

    @Autowired
    private QuartzChannel quartzChannel;

    public void initTask() {

        Map<String, Object> map = applicationContext.getBeansWithAnnotation(IrsQuartz.class);
        for (Map.Entry<String, Object> t : map.entrySet()) {
            Object object = t.getValue();
             if(!isImplJob(object.getClass())){
                continue;
            }
            IrsQuartz irsQuartz = object.getClass().getDeclaredAnnotation(IrsQuartz.class);
            sendJob(irsQuartz,t.getKey());
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
     * @param irsQuartz
     */
    private  void   sendJob(IrsQuartz irsQuartz,String beanName){

        TaskModel taskModel = new TaskModel();
        taskModel.setJobName(irsQuartz.jobName());
        taskModel.setTaskGroup(group);
        taskModel.setCronExpression(irsQuartz.cronExpression());
        taskModel.setRetryCount(irsQuartz.retryCount());
        taskModel.setBeanName(beanName);
        quartzChannel.sendJobToChannel(taskModel);
    }

}
