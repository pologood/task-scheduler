package com.games.job.server.job;

import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.server.ApplicationTest;
import com.games.job.server.entity.Task;
import com.games.job.server.entity.TaskRecord;
import com.games.job.server.repository.TaskRecordRepository;
import com.games.job.server.repository.TaskRepository;
import com.games.job.server.service.TaskService;
import org.junit.Assert;
import org.junit.Test;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ScheduledJobTest extends ApplicationTest {

    @Autowired
    private ScheduledJob scheduledJob;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskRecordRepository taskRecordRepository;

    @Autowired
    private TaskService taskService;

    @Test
   public void  task_initJob() throws JobExecutionException {
        TaskModel taskModel = new TaskModel();
        taskModel.setJobName("sendInitJob");
        taskModel.setJobGroup("irs-crm-server");
        taskModel.setBeanName("testBeanName");
        taskModel.setCronExpression("0 0/1 * * * ?");
        taskModel.setRetryCount(5);
        Task task = taskRepository.findByModuleAndJobGroupAndJobName(taskModel.getModule(),taskModel.getJobGroup(),taskModel.getJobName());
        if(task!=null){
            taskRepository.delete(task.getId());
        }
        taskService.addOrModQuartz(taskModel);
        taskRecordRepository.deleteAll();

        Task task1 = taskRepository.findByModuleAndJobGroupAndJobName(taskModel.getModule(),taskModel.getJobGroup(),taskModel.getJobName());
        scheduledJob.dealScheduledJob(task1.getId());
        Task result = taskRepository.findByModuleAndJobGroupAndJobName(taskModel.getModule(),taskModel.getJobGroup(),taskModel.getJobName());

        Assert.assertTrue(result.getStatus().intValue()== TaskStatus.SEND.getId());
        List<TaskRecord> taskRecords = taskRecordRepository.findByTaskId(result.getId());
        Assert.assertTrue(CollectionUtils.isEmpty(taskRecords));
        taskService.delQuartz(result.getId());
    }

   @Test
   public void  task_notInitJob(){

       TaskModel taskModel = new TaskModel();
       taskModel.setJobName("notInitJobTest");
       taskModel.setJobGroup("my-server");
       taskModel.setBeanName("testBeanName");
       taskModel.setCronExpression("0 0/5 * * * ?");
       taskModel.setRetryCount(5);
       Task task = taskRepository.findByModuleAndJobGroupAndJobName(taskModel.getModule(),taskModel.getJobGroup(),taskModel.getJobName());
       if(task!=null){
           taskRepository.delete(task.getId());
       }
       taskService.addOrModQuartz(taskModel);

       Task task1 = taskRepository.findByModuleAndJobGroupAndJobName(taskModel.getModule(),taskModel.getJobGroup(),taskModel.getJobName());
       task1.setStatus(TaskStatus.NOFEEDBACK.getId());
       taskRepository.save(task1);

       taskRecordRepository.deleteAll();
       scheduledJob.dealScheduledJob(task1.getId());

       Task result = taskRepository.findByModuleAndJobGroupAndJobName(taskModel.getModule(),taskModel.getJobGroup(),taskModel.getJobName());
       Assert.assertTrue(result.getStatus().intValue()== TaskStatus.SEND.getId());

       List<TaskRecord> taskRecords = taskRecordRepository.findByTaskId(result.getId());
       Assert.assertTrue(taskRecords.size()==1);
       taskService.delQuartz(result.getId());
   }
}
