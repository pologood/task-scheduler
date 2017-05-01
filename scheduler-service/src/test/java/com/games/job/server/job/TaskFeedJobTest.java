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

public class TaskFeedJobTest extends ApplicationTest {

    @Autowired
    private TaskFeedJob taskStatusMonitorJob;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public  void   test_taskTimeOut() throws JobExecutionException {
        Task oldTask = new Task();
        oldTask.setJobGroup("my-server8");
        oldTask.setJobName("sendAndTimeOutTask");
        oldTask.setBeanName("testBeanName");
        oldTask.setCronExpression("0 0/5 * * * ?");
        oldTask.setSendTime(DateUtils.addMinutes(new Date(),-10));
        oldTask.setRetryCount(5);
        oldTask.setRetryCounted(0);
        oldTask.setStatus(TaskStatus.SEND.getId());
        Task task = taskRepository.findByModuleAndJobGroupAndJobName(oldTask.getModule(),oldTask.getJobGroup(),oldTask.getJobName());
        if(task!=null){
            taskRepository.delete(task.getId());
        }
        taskRepository.save(oldTask);

        taskStatusMonitorJob.execute(null);
        Task resultTask = taskRepository.findById(oldTask.getId());
        Assert.assertTrue(resultTask.getStatus().intValue()==TaskStatus.NOFEEDBACK.getId());
        Assert.assertTrue(resultTask.getRetryCounted().intValue()==1);
    }

    @Test
    public void  test_taskRetryFail() throws JobExecutionException {

        Task oldTask = new Task();
        oldTask.setJobName("dealNoFeedBackStatusTaskTest");
        oldTask.setJobGroup("MY-server");
        oldTask.setBeanName("testBeanName");
        oldTask.setCronExpression("0 0/5 * * * ?");
        oldTask.setSendTime(DateUtils.addMinutes(new Date(),-10));
        oldTask.setRetryCount(5);
        oldTask.setRetryCounted(5);
        oldTask.setStatus(TaskStatus.NOFEEDBACK.getId());
        Task task = taskRepository.findByModuleAndJobGroupAndJobName(oldTask.getModule(),oldTask.getJobGroup(),oldTask.getJobName());
        if(task!=null){
            taskRepository.delete(task.getId());
        }
        taskRepository.save(oldTask);

        taskStatusMonitorJob.execute(null);
        Task resultTask = taskRepository.findById(oldTask.getId());
        Assert.assertTrue(resultTask.getStatus().intValue()==TaskStatus.RETRYFAIL.getId());
        Assert.assertTrue(resultTask.getRetryCounted().intValue()==5);
    }


}
