package com.games.job.server.task;

import java.util.Date;

import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.server.entity.Task;
import com.games.job.server.entity.TaskRecord;
import com.games.job.server.repository.TaskRecordRepository;
import com.games.job.server.repository.TaskRepository;
import com.games.job.common.utils.BeanUtils;
import com.games.job.server.service.producer.RedisJobProducer;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJob implements Job {
    /**
     * 将任务发到redis执行
     */
    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    @Autowired
    private RedisJobProducer redisJobProducer;

    @Autowired
    private TaskRecordRepository taskRecordRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Integer taskId = dataMap.getIntFromString("taskId");
        dealScheduledJob(taskId);
    }

    public void dealScheduledJob(Integer taskId) {
        if(taskId==null){
            logger.error("ScheduledJob@execute - taskId is null");
            return;
        }
        Task task = taskRepository.findOne(taskId);
        addLastTaskRecord(task);
        initThisTimeTask(task);
        TaskModel taskModel = new TaskModel();
        taskModel.setBeanName(task.getBeanName());
        taskModel.setTaskId(task.getId());
        taskModel.setTaskGroup(task.getTaskGroup());
        taskModel.validNotifyTaskModel();
        redisJobProducer.notifyTaskInstance(taskModel);
    }

    private void initThisTimeTask(Task task) {

        task.setEndTime(null);
        task.setRetryCounted(0);
        task.setBeginTime(null);
        task.setStatus(TaskStatus.SEND.getId());
        task.setSendTime(new Date());
        task.setCreateTime(new Date());
        taskRepository.save(task);
    }

    private void addLastTaskRecord(Task task) {

        if (task.getStatus() == TaskStatus.INIT.getId()) {
            return;
        }
        TaskRecord taskRecord = new TaskRecord();
        BeanUtils.copyProperties(task, taskRecord);
        taskRecord.setTaskId(task.getId());
        taskRecord.setId(null);
        taskRecordRepository.save(taskRecord);
    }

}
