package com.games.job.server.job;

import com.games.job.common.enums.TaskStatus;
import com.games.job.server.ApplicationTest;
import com.games.job.server.entity.Task;
import com.games.job.server.repository.TaskRepository;
import com.games.job.common.utils.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by wangshichao on 2016/11/21.
 */
public class TaskStatusMonitorJobTest extends ApplicationTest {

    @Autowired
    private TaskRetryJob taskStatusMonitorJob;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public  void   dealHaveSendStatusAndTimeOutTaskTest() throws JobExecutionException {

        Task oldTask = new Task();
        oldTask.setJobName("TaskStatusMonitorJob");
        oldTask.setTaskGroup("irs-crm-server");
        oldTask.setBeanName("testBeanName");
        oldTask.setCronExpression("0 0/5 * * * ?");
        oldTask.setSendTime(DateUtils.addMinutes(new Date(),-10));
        oldTask.setRetryCount(5);
        oldTask.setRetryCounted(0);
        oldTask.setStatus(TaskStatus.SEND.getId());
        Task task = taskRepository.findByTaskGroupAndJobName(oldTask.getTaskGroup(),oldTask.getJobName());
        if(task!=null){
            taskRepository.delete(task.getId());
        }
        taskRepository.save(oldTask);
        taskStatusMonitorJob.execute(null);
        Task resultTask = taskRepository.findOne(oldTask.getId());
        Assert.assertTrue(resultTask.getStatus().intValue()==TaskStatus.NOFEEDBACK.getId());
        Assert.assertTrue(resultTask.getRetryCounted().intValue()==1);
    }

    @Test
    public void  dealNoFeedBackStatusTaskTest() throws JobExecutionException {

        Task oldTask = new Task();
        oldTask.setJobName("dealNoFeedBackStatusTaskTest");
        oldTask.setTaskGroup("irs-crm-server");
        oldTask.setBeanName("testBeanName");
        oldTask.setCronExpression("0 0/5 * * * ?");
        oldTask.setSendTime(DateUtils.addMinutes(new Date(),-10));
        oldTask.setRetryCount(5);
        oldTask.setRetryCounted(5);
        oldTask.setStatus(TaskStatus.NOFEEDBACK.getId());
        Task task = taskRepository.findByTaskGroupAndJobName(oldTask.getTaskGroup(),oldTask.getJobName());
        if(task!=null){
            taskRepository.delete(task.getId());
        }
        taskRepository.save(oldTask);
        taskStatusMonitorJob.execute(null);
        Task resultTask = taskRepository.findOne(oldTask.getId());
        Assert.assertTrue(resultTask.getStatus().intValue()==TaskStatus.RETRYFAIL.getId());
        Assert.assertTrue(resultTask.getRetryCounted().intValue()==5);
    }


}
