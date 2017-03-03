package com.games.job.server.service;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;
import java.util.Set;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.BeanUtils;
import com.games.job.server.entity.Task;
import com.games.job.server.repository.TaskRepository;
import com.games.job.server.job.ScheduledJob;

/**
 * Created by wangshichao on 2016/11/15.
 */
@Service
public class TaskService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private TaskRepository taskRepository;

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    /**
     * 创建定时任务
     * @param taskModel
     * @return
     */
    @Transactional
    public void addOrModQuartz(TaskModel taskModel){
        try{
            this.addOrModTask(taskModel);
            this.addOrModJob(taskModel);
        }catch (SchedulerException e){
            log.error("@addOrUpdateJob - create or update job error task - para:{}",taskModel,e);
            throw new RuntimeException("create or update job error");
        }
    }
    private void  addOrModTask(TaskModel taskModel){
        Task task =  taskRepository.findByTaskGroupAndJobName(taskModel.getTaskGroup(),taskModel.getJobName());
        if(task==null){
            task = new Task();
        }
        BeanUtils.copyProperties(taskModel,task);
        task.setStatus(TaskStatus.INIT.getId());
        if(task.getRetryCount()==null){
            task.setRetryCount(0);
        }
        task.setRetryCounted(0);
        taskRepository.save(task);
        taskModel.setTaskId(task.getId());
    }
    private void addOrModJob(TaskModel taskModel) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetail jobDetail = newJob(ScheduledJob.class)
                .withIdentity(taskModel.getJobName(), taskModel.getTaskGroup())
                .usingJobData("taskId",taskModel.getTaskId()+"")
                .build();
        CronTrigger trigger = newTrigger()
                .withIdentity(taskModel.getJobName(), taskModel.getTaskGroup())
                .startNow()
                .withSchedule(cronSchedule(taskModel.getCronExpression()))
                .build();
        if(scheduler.checkExists(jobDetail.getKey())){
            modJob(taskModel);
        }else {
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }


    private void modJob(TaskModel taskModel) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(
                taskModel.getJobName(), taskModel.getTaskGroup()
        );//从Task中得到triggerKey
        CronTrigger trigger = (CronTrigger) scheduler
                .getTrigger(triggerKey);//从Scheduler中获取该trigger,通过triggerKey
        trigger = trigger.getTriggerBuilder()
                .withIdentity(taskModel.getJobName(), taskModel.getTaskGroup())
                .startNow()
                .withSchedule(cronSchedule(taskModel.getCronExpression()))
                .build();
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 删除定时任务
     * @param id
     */
    public void delQuartz(Integer id){
        try{
            Task task = taskRepository.findOne(id);
            JobKey jobKey = JobKey.jobKey(task.getJobName(),task.getTaskGroup());
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.deleteJob(jobKey);
            taskRepository.delete(id);
        }catch (Exception e){
            log.error("@deleteJob - delete job error - para:{}",id,e);
            throw new RuntimeException("create job error task");
        }
    }

    /**
     * 初始化定时任务
     * @param set
     */
    public void initQuartzs(Set<TaskModel> set ){
        set.stream().forEach(taskModel -> {
            this.addOrModQuartz(taskModel);
        });
    }



    public List<Task>  getTasks(){
        return taskRepository.findAll();
    }


    public Task getTask(Integer id){
        return taskRepository.findOne(id);
    }

    public void modTasksStatus( List<TaskModel>  list){
        list.stream().forEach(taskModel -> {
            Task task = taskRepository.findOne(taskModel.getTaskId());
            BeanUtils.copyProperties(taskModel,task);
            taskRepository.save(task);
        });
    }
}

