package com.games.job.client.service.producer;

import com.games.job.client.annotation.Quartz;
import com.games.job.client.annotation.QuartzRestful;
import com.games.job.client.job.Job;
import com.games.job.common.constant.Constants;
import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.NetUtils;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${server.port")
    private Integer port=8080;

    @Value("${server.servlet-path")
    private String servletPath="/";

    private static final Logger log = LoggerFactory.getLogger(JobProducer.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        initTask();
        log.info("TaskInitBean has  init");
    }

    public void initTask() {
        Map<String, Object> quartzMap = applicationContext.getBeansWithAnnotation(Quartz.class);
        for (Map.Entry<String, Object> entry : quartzMap.entrySet()) {
            Object object = entry.getValue();
            if(!isImplJob(object.getClass())){
                continue;
            }
            Quartz quartz = object.getClass().getDeclaredAnnotation(Quartz.class);
            TaskModel taskModel = new TaskModel();
            taskModel.setTaskGroup(quartz.groupName());
            taskModel.setJobName(quartz.jobName());
            taskModel.setCronExpression(quartz.cronExpression());
            taskModel.setRetryCount(quartz.retryCount());
            taskModel.setBeanName(entry.getKey());
            sendJob(taskModel);
        }

        //处理 QuartzRestful
        Map<String, Object> quartzRestfulMap = applicationContext.getBeansWithAnnotation(QuartzRestful.class);
        for (Map.Entry<String, Object> entry : quartzRestfulMap.entrySet()) {
            Object object = entry.getValue();
            if(!isImplJob(object.getClass())){
                continue;
            }
            QuartzRestful quartzRestful = object.getClass().getDeclaredAnnotation(QuartzRestful.class);
            TaskModel taskModel = new TaskModel();
            taskModel.setTaskGroup(quartzRestful.groupName());
            taskModel.setJobName(quartzRestful.jobName());
            taskModel.setCronExpression(quartzRestful.cronExpression());
            taskModel.setRetryCount(quartzRestful.retryCount());
            taskModel.setBeanName(entry.getKey());
            boolean userRestful = quartzRestful.useRestful();
            String url = quartzRestful.url();
            Preconditions.checkArgument(userRestful,"userRestful is must true");
            StringBuffer path = new StringBuffer(Constants.SCHEMA);
            path.append(NetUtils.getPrivateAddress()).append(":").append(port)
                    .append(servletPath).append("/");
            if(StringUtils.isNotBlank(url)){
                path.append(url);
            }else{
                //todo 通过注解获取url
            }
            taskModel.setPath(path.toString());
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
