package com.games.job.client.aop;

import com.games.job.client.annotation.QuartzRestful;
import com.games.job.client.service.TaskManager;
import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.google.common.base.Preconditions;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author:liujh
 * @create_time:2017/2/24 18:33
 * @project:job-center
 * @full_name:com.games.job.client.aop.QuartzRestfulHandler
 * @ide:IntelliJ IDEA
 *
 * handle annotation QuartzRestful {@link com.games.job.client.annotation.QuartzRestful })
 */

@Component
@Aspect
public class QuartzRestfulHandler {

    private final static Logger logger = LoggerFactory.getLogger(QuartzRestfulHandler.class);

    @Autowired
    private TaskManager taskManager;

    @Around(value = "@annotation(quartzRestful)")
    public Object around(ProceedingJoinPoint pjp, QuartzRestful quartzRestful){
        Integer taskId=0;
        try {
            Object[] args = pjp.getArgs();
            Preconditions.checkArgument(args!=null&&args.length==1,"task method must have only one param.");
            taskId = (Integer) args[0];
            sendBeginStatus(taskId);
            Object ret = pjp.proceed();
            sendEndStatus(taskId);
            return ret;
        } catch (Throwable e) {
            if(taskId!=0){
                sendFailStatus(taskId);
            }
            return  null;
        }
    }

    private void sendBeginStatus(Integer taskId) {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setStatus(TaskStatus.BEGIN.getId());
        taskModel.setBeginTime(new Date());
        taskModel.initDealTime();
        taskManager.sendBeginStatus(taskModel);
    }

    private void sendEndStatus(Integer taskId) {
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setEndTime(new Date());
        taskModel.setStatus(TaskStatus.END.getId());
        taskModel.initDealTime();
        taskManager.sendEndStatus(taskModel);
    }

    public void sendFailStatus(Integer taskId) {
        TaskModel taskModel = new TaskModel();
        taskModel.setStatus(TaskStatus.FAIL.getId());
        taskModel.setEndTime(new Date());
        taskModel.initDealTime();
        taskManager.sendFailStatus(taskModel);
    }

}
