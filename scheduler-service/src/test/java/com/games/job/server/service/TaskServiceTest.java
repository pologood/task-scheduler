package com.games.job.server.service;

import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.server.ApplicationTest;
import com.games.job.server.entity.Task;
import com.games.job.server.repository.TaskRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

public class TaskServiceTest extends ApplicationTest{

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void  task_addQuartzJob() {
        TaskModel taskModel = new TaskModel();
        taskModel.setModule("co-channel");
        taskModel.setJobName("testJob");
        taskModel.setJobGroup("group1");
        taskModel.setBeanName("testBeanName");
        taskModel.setCronExpression("0 0/1 * * * ?");
        taskModel.setRetryCount(5);
        Task task = taskRepository.findByModuleAndJobGroupAndJobName(taskModel.getModule(),taskModel.getJobGroup(),taskModel.getJobName());
        if(task!=null){
            taskRepository.delete(task.getId());
        }
        taskService.addOrModQuartz(taskModel);
        Task insertTask = taskRepository.findByModuleAndJobGroupAndJobName(taskModel.getModule(),taskModel.getJobGroup(),taskModel.getJobName());
        Assert.assertTrue(insertTask.getStatus().intValue()== TaskStatus.INIT.getId());
        Assert.assertTrue(insertTask.getRetryCount().intValue()==5);
        Assert.assertTrue(insertTask.getBeanName().equals(taskModel.getBeanName()));
    }

    @Test
    public void  task_modQuartzJob()  throws IOException{
        Task oldTask = new Task();
        oldTask.setJobName("testJob1");
        oldTask.setJobGroup("myServer1");
        oldTask.setBeanName("testBeanName");
        oldTask.setCronExpression("0 0/5 * * * ?");
        oldTask.setRetryCount(5);
        oldTask.setRetryCounted(0);
        oldTask.setStatus(TaskStatus.INIT.getId());
        oldTask.setCreateTime(new Date());
        Task task = taskRepository.findByModuleAndJobGroupAndJobName(oldTask.getModule(),oldTask.getJobGroup(),oldTask.getJobName());
        if(task==null){
            taskRepository.save(oldTask);
        }
        TaskModel taskModel = new TaskModel();
        taskModel.setJobName("testJob1");
        taskModel.setJobGroup("myServer1");
        taskModel.setBeanName("testBeanName");
        taskModel.setCronExpression("0 0/1 * * * ?");
        taskModel.setRetryCount(2);
        oldTask.setCreateTime(new Date());
        taskService.addOrModQuartz(taskModel);
        Task insertTask = taskRepository.findByModuleAndJobGroupAndJobName(taskModel.getModule(),taskModel.getJobGroup(),taskModel.getJobName());
        Assert.assertTrue(insertTask.getCronExpression().equals(taskModel.getCronExpression()));
        Assert.assertTrue(insertTask.getRetryCount().intValue()==taskModel.getRetryCount().intValue());
    }

    @Test
    public void  test_delQuartzJob(){
        Task oldTask = new Task();
        oldTask.setJobName("testDeleteJob");
        oldTask.setJobGroup("myServer");
        oldTask.setBeanName("testBeanName");
        oldTask.setCronExpression("0 0/1 * * * ?");
        oldTask.setRetryCount(5);
        oldTask.setRetryCounted(0);
        oldTask.setStatus(0);
        Task task = taskRepository.findByModuleAndJobGroupAndJobName(oldTask.getModule(),oldTask.getJobGroup(),oldTask.getJobName());
        if(task==null){
            taskRepository.save(oldTask);
        }
        Task jobGroupAndJobName = taskRepository.findByModuleAndJobGroupAndJobName(oldTask.getModule(),oldTask.getJobGroup(),oldTask.getJobName());
        Assert.assertNotNull(jobGroupAndJobName);
        taskService.delQuartz(jobGroupAndJobName.getId());
        Task byIdTask  =  taskRepository.findById(jobGroupAndJobName.getId());
        Assert.assertNull(byIdTask);
    }


    @Test
    public void  test_modTaskStatus_batch(){
        Task oldTask = new Task();
        oldTask.setJobName("testDeleteJob");
        oldTask.setJobGroup("myServer");
        oldTask.setBeanName("testBeanName");
        oldTask.setCronExpression("0 0/5 * * * ?");
        oldTask.setRetryCount(5);
        oldTask.setRetryCounted(0);
        oldTask.setStatus(TaskStatus.INIT.getId());
        Task task = taskRepository.findByModuleAndJobGroupAndJobName(oldTask.getModule(),oldTask.getJobGroup(),oldTask.getJobName());
        if(task!=null){
            taskRepository.delete(task.getId());
        }
        taskRepository.save(oldTask);
        Task jobGroupAndJobName = taskRepository.findByModuleAndJobGroupAndJobName(oldTask.getModule(),oldTask.getJobGroup(),oldTask.getJobName());

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(jobGroupAndJobName.getId());
        taskModel.setStatus(TaskStatus.FAIL.getId());
        taskModel.setEndTime(new Date());
        List<TaskModel> list  =  new ArrayList<>();
        list.add(taskModel);
        taskService.modTasksStatus(list);
        Task byIdTask = taskRepository.findById(jobGroupAndJobName.getId());
        Assert.assertTrue(byIdTask.getStatus()==TaskStatus.FAIL.getId());
    }

}
