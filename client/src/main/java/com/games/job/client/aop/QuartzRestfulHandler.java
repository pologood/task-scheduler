package com.games.job.client.aop;

import com.games.job.client.annotation.QuartzRestful;
import com.games.job.client.service.channel.Channel;
import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
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
    private Channel restfulChannel;

    @Around(value = "@annotation(quartzRestful)")
    public Object around(ProceedingJoinPoint pjp, QuartzRestful quartzRestful){
        try {
            Integer taskId = 0;
            // TODO: 2017/2/24 获取参数taskId 
            sendBeginStatus(taskId);
            Object ret = pjp.proceed();
            sendEndStatus(taskId);
            return ret;
        } catch (Throwable e) {
            return  null;
        }
    }

    private void sendBeginStatus(Integer taskId) {
        TaskModel beginTaskModel = new TaskModel();
        beginTaskModel.setTaskId(taskId);
        beginTaskModel.setStatus(TaskStatus.BEGIN.getId());
        beginTaskModel.setBeginTime(new Date());
        beginTaskModel.initDealTime();
        restfulChannel.sendTaskStatus(beginTaskModel);
    }

    private void sendEndStatus(Integer taskId) {
        TaskModel endTaskModel = new TaskModel();
        endTaskModel.setTaskId(taskId);
        endTaskModel.setEndTime(new Date());
        endTaskModel.setStatus(TaskStatus.END.getId());
        endTaskModel.initDealTime();
        restfulChannel.sendTaskStatus(endTaskModel);
    }

}
