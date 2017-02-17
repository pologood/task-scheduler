package com.games.job.server.service;

import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.server.ApplicationTest;
import com.games.job.server.entity.Task;
import com.games.job.server.repository.TaskRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by wangshichao on 2016/11/22.
 */
public class TaskServiceTest extends ApplicationTest{

    @Autowired
    private TaskService  taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void  addJobTest(){
        TaskModel taskModel = new TaskModel();
        taskModel.setJobName("testJob");
        taskModel.setTaskGroup("irs-crm-server");
        taskModel.setBeanName("testBeanName");
        taskModel.setCronExpression("0 0/5 * * * ?");
        taskModel.setRetryCount(5);
        Task task = taskRepository.findByTaskGroupAndJobName(taskModel.getTaskGroup(),taskModel.getJobName());
        if(task!=null){
            taskRepository.delete(task.getId());
        }
        taskService.addOrUpdateJob(taskModel);
        Task insertTask = taskRepository.findByTaskGroupAndJobName(taskModel.getTaskGroup(),taskModel.getJobName());
        Assert.assertTrue(insertTask.getStatus().intValue()== TaskStatus.INIT.getId());
        Assert.assertTrue(insertTask.getRetryCount().intValue()==5);
        Assert.assertTrue(insertTask.getBeanName().equals(taskModel.getBeanName()));
    }

    @Test
    public void  updateJobTest(){

        Task oldTask = new Task();
        oldTask.setJobName("testJob");
        oldTask.setTaskGroup("irs-crm-server");
        oldTask.setBeanName("testBeanName");
        oldTask.setCronExpression("0 0/5 * * * ?");
        oldTask.setRetryCount(5);
        Task task = taskRepository.findByTaskGroupAndJobName(oldTask.getTaskGroup(),oldTask.getJobName());
        if(task==null){
            taskRepository.save(oldTask);
        }
        TaskModel taskModel = new TaskModel();
        taskModel.setJobName("testJob");
        taskModel.setTaskGroup("irs-crm-server");
        taskModel.setBeanName("testBeanName");
        taskModel.setCronExpression("0 0/6 * * * ?");
        taskModel.setRetryCount(6);
        taskService.addOrUpdateJob(taskModel);
        Task insertTask = taskRepository.findByTaskGroupAndJobName(taskModel.getTaskGroup(),taskModel.getJobName());
        Assert.assertTrue(insertTask.getCronExpression().equals(taskModel.getCronExpression()));
        Assert.assertTrue(insertTask.getRetryCount().intValue()==taskModel.getRetryCount().intValue());
    }

    @Test
    public void  deleteJobById(){

        Task oldTask = new Task();
        oldTask.setJobName("testDeleteJob");
        oldTask.setTaskGroup("irs-crm-server");
        oldTask.setBeanName("testBeanName");
        oldTask.setCronExpression("0 0/5 * * * ?");
        oldTask.setRetryCount(5);
        oldTask.setRetryCounted(0);
        oldTask.setStatus(0);
        Task task = taskRepository.findByTaskGroupAndJobName(oldTask.getTaskGroup(),oldTask.getJobName());
        if(task==null){
            taskRepository.save(oldTask);
        }
        Task taskGroupAndJobName = taskRepository.findByTaskGroupAndJobName(oldTask.getTaskGroup(),oldTask.getJobName());
        taskService.deleteJob(taskGroupAndJobName.getId());
        Task byIdTask  =  taskRepository.findOne(taskGroupAndJobName.getId());
        Assert.assertNull(byIdTask);
    }

    @Test
    public void  batchUpdateTaskMachineStatusTest(){

        Task oldTask = new Task();
        oldTask.setJobName("testDeleteJob");
        oldTask.setTaskGroup("irs-crm-server");
        oldTask.setBeanName("testBeanName");
        oldTask.setCronExpression("0 0/5 * * * ?");
        oldTask.setRetryCount(5);
        oldTask.setRetryCounted(0);
        oldTask.setStatus(TaskStatus.INIT.getId());
        Task task = taskRepository.findByTaskGroupAndJobName(oldTask.getTaskGroup(),oldTask.getJobName());
        if(task!=null){
            taskRepository.delete(task.getId());
        }
        taskRepository.save(oldTask);
        Task taskGroupAndJobName = taskRepository.findByTaskGroupAndJobName(oldTask.getTaskGroup(),oldTask.getJobName());
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskGroupAndJobName.getId());
        taskModel.setStatus(TaskStatus.FAIL.getId());
        taskModel.setEndTime(new Date());
        List<TaskModel> list  =  new ArrayList<>();
        list.add(taskModel);
        taskService.batchUpdateTaskMachineStatus(list);
        Task byIdTask = taskRepository.findOne(taskGroupAndJobName.getId());
        Assert.assertTrue(byIdTask.getStatus()==TaskStatus.FAIL.getId());
    }

}
