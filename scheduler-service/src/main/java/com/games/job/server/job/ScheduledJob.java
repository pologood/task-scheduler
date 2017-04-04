package com.games.job.server.job;

import java.util.Date;
import java.util.Map;

import com.games.job.common.enums.HttpMethod;
import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;
import com.games.job.common.utils.NetUtils;
import com.games.job.server.entity.Task;
import com.games.job.server.entity.TaskRecord;
import com.games.job.server.entity.restful.Result;
import com.games.job.server.enums.ResponseCode;
import com.games.job.server.repository.TaskRecordRepository;
import com.games.job.server.repository.TaskRepository;
import com.games.job.common.utils.BeanUtils;
import com.games.job.server.service.TaskManager;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduledJob implements Job {
    /**
     * 将任务发到channel(redis,mq)执行
     * httpclient调用restful接口执行任务
     */
    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    @Autowired
    private TaskManager taskManager;

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
        if(task==null){
            logger.error("ScheduledJob@execute - task is null");
            return;
        }
        addLastTaskRecord(task);
        initThisTimeTask(task);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(task.getId());
        taskModel.setJobGroup(task.getJobGroup());
        taskModel.setJobName(task.getJobName());
        taskModel.setBeanName(task.getBeanName());
        taskModel.setPath(task.getPath());
        taskModel.validNotifyTaskModel();
        taskManager.sendTask(taskModel);
    }

    private void initThisTimeTask(Task task) {
        task.setBeginTime(null);
        task.setEndTime(null);
        task.setRetryCounted(0);
        task.setStatus(TaskStatus.SEND.getId());
        task.setSendTime(new Date());
        taskRepository.save(task);
    }

    private void addLastTaskRecord(Task task) {
        if(task.getStatus()==null||task.getStatus().intValue()==TaskStatus.INIT.getId()){
            return;
        }
        TaskRecord taskRecord = new TaskRecord();
        BeanUtils.copyProperties(task, taskRecord);
        taskRecord.setTaskId(task.getId());
        taskRecord.setId(null);
        taskRecordRepository.save(taskRecord);
    }

}
