package com.games.job.client.service;

import com.games.job.client.annotation.Quartz;
import com.games.job.client.annotation.QuartzRestful;
import com.games.job.client.job.Job;
import com.games.job.common.constant.Constants;
import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.NetUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author:liujh
 * @create_time:2017/2/17 18:13
 * @project:job-center
 * @full_name:com.games.job.client.service.reporter.ReporterAdapter
 * @ide:IntelliJ IDEA
 */
@Component
public class TaskAgent implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private TaskManager taskManager;

    @Value("${spring.quartz.group}")
    private String quartzGroup= Constants.TASK_GROUP_NAME;

    @Value("${server.port}")
    private Integer serverPort=8080;
    @Value("${server.servlet-path}")
    private String serverPath="/";

    private static final Logger log = LoggerFactory.getLogger(TaskAgent.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        initTask();
        log.info("TaskInitBean has  init");
    }

    public void initTask() {
        List<TaskModel> taskModels = Lists.newArrayList();
        Map<String, Object> quartzMap = applicationContext.getBeansWithAnnotation(Quartz.class);
        for (Map.Entry<String, Object> entry : quartzMap.entrySet()) {
            Object object = entry.getValue();
            if(!isImplJob(object.getClass())){
                continue;
            }
            Quartz quartz = object.getClass().getDeclaredAnnotation(Quartz.class);
            TaskModel taskModel = new TaskModel();
            taskModel.setTaskGroup(quartzGroup);
            taskModel.setJobName(quartz.jobName());
            taskModel.setCronExpression(quartz.cronExpression());
            taskModel.setRetryCount(quartz.retryCount());
            taskModel.setBeanName(entry.getKey());
            taskModels.add(taskModel);
        }

        //处理 QuartzRestful
        Map<String, Object> quartzRestfulMap = applicationContext.getBeansWithAnnotation(QuartzRestful.class);
        for (Map.Entry<String, Object> entry : quartzRestfulMap.entrySet()) {
            Object object = entry.getValue();
            QuartzRestful quartzRestful = object.getClass().getDeclaredAnnotation(QuartzRestful.class);
            TaskModel taskModel = new TaskModel();
            taskModel.setTaskGroup(quartzGroup);
            taskModel.setJobName(quartzRestful.jobName());
            taskModel.setCronExpression(quartzRestful.cronExpression());
            taskModel.setRetryCount(quartzRestful.retryCount());
            taskModel.setBeanName(entry.getKey());
            boolean userRestful = quartzRestful.useRestful();
            String url = quartzRestful.url();
            Preconditions.checkArgument(userRestful,"userRestful is must true");
            StringBuffer path = new StringBuffer(Constants.SCHEMA);
            path.append(NetUtils.getPrivateAddress()).append(":")
                    .append(serverPort).append(serverPath).append("/");
            if(StringUtils.isNotBlank(url)){
                path.append(url);
            }
            taskModel.setPath(path.toString());
            taskModels.add(taskModel);
        }
        taskManager.sendTasks(taskModels);
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

}
