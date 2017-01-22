package com.games.job.server.task;

import com.games.job.server.ApplicationTest;
import com.games.job.server.Task.ScheduledJob;
import com.games.job.server.entity.Task;
import com.games.job.server.entity.TaskRecord;
import com.games.job.server.enums.TaskStatus;
import com.games.job.server.model.TaskModel;
import com.games.job.server.repository.TaskRecordRepository;
import com.games.job.server.repository.TaskRepository;
import com.games.job.server.service.TaskService;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by wangshichao on 2016/11/22.
 */
public class ScheduledJobTaskTest extends ApplicationTest {

    @Autowired
    private ScheduledJob scheduledJob;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskRecordRepository taskRecordRepository;

    @Autowired
    private TaskService taskService;

    @Test
   public void  sendInitJob() throws JobExecutionException {

        TaskModel taskModel = new TaskModel();
        taskModel.setJobName("sendInitJob");
        taskModel.setTaskGroup("irs-crm-server");
        taskModel.setBeanName("testBeanName");
        taskModel.setCronExpression("0 0/5 * * * ?");
        taskModel.setRetryCount(5);
        Task task = taskRepository.findByTaskGroupAndJobName(taskModel.getTaskGroup(),taskModel.getJobName());
        if(task!=null){
            taskRepository.delete(task.getId());
        }
        taskService.addOrUpdateJob(taskModel);
        Task task1 = taskRepository.findByTaskGroupAndJobName(taskModel.getTaskGroup(),taskModel.getJobName());
        taskRecordRepository.deleteAll();
        scheduledJob.dealScheduledJob(task1.getId());
        Task result = taskRepository.findByTaskGroupAndJobName(taskModel.getTaskGroup(),taskModel.getJobName());
        Assert.assertTrue(result.getStatus().intValue()== TaskStatus.SEND.getId());
        List<TaskRecord> taskRecords = taskRecordRepository.findByTaskId(result.getId());
        Assert.assertTrue(CollectionUtils.isEmpty(taskRecords));
        taskService.deleteJob(result.getId());
    }

   @Test
   public void  sendNoInitStatusJobTest(){

       TaskModel taskModel = new TaskModel();
       taskModel.setJobName("sendNoInitStatusJobTest");
       taskModel.setTaskGroup("irs-crm-server");
       taskModel.setBeanName("testBeanName");
       taskModel.setCronExpression("0 0/5 * * * ?");
       taskModel.setRetryCount(5);
       Task task = taskRepository.findByTaskGroupAndJobName(taskModel.getTaskGroup(),taskModel.getJobName());
       if(task!=null){
           taskRepository.delete(task.getId());
       }
       taskService.addOrUpdateJob(taskModel);
       Task task1 = taskRepository.findByTaskGroupAndJobName(taskModel.getTaskGroup(),taskModel.getJobName());
       task1.setStatus(TaskStatus.NOFEEDBACK.getId());
       taskRepository.save(task1);
       taskRecordRepository.deleteAll();
       scheduledJob.dealScheduledJob(task1.getId());
       Task result = taskRepository.findByTaskGroupAndJobName(taskModel.getTaskGroup(),taskModel.getJobName());
       Assert.assertTrue(result.getStatus().intValue()== TaskStatus.SEND.getId());
       List<TaskRecord> taskRecords = taskRecordRepository.findByTaskId(result.getId());
       Assert.assertTrue(taskRecords.size()==1);
       taskService.deleteJob(result.getId());
   }
}
