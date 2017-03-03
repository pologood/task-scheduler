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
        System.out.println("===============ljh"+new Date());
        addLastTaskRecord(task);
        initThisTimeTask(task);

        String path = task.getPath();
        if(StringUtils.isNotBlank(path)){ //restful 触发任务
           retry(path,taskId,null);
        }else{
            TaskModel taskModel = new TaskModel();
            taskModel.setBeanName(task.getBeanName());
            taskModel.setTaskId(task.getId());
            taskModel.setTaskGroup(task.getTaskGroup());
            taskModel.validNotifyTaskModel();
            taskManager.sendTask(taskModel);
        }
    }

    private String fireRestfulJob(String path,Integer taskId){
        Map<String,String> params = Maps.newConcurrentMap();
        params.put("id",taskId+"");
        return NetUtils.sendMap(path,params,HttpMethod.GET);
    }

    private void retry(String path,Integer taskId,Task task){
        if(task==null){
            task = taskRepository.getOne(taskId);
        }
        if(task!=null){
            if(task.getRetryCount() > task.getRetryCounted()){
                try {
                    String result = fireRestfulJob(path,taskId);
                    if(StringUtils.isNotBlank(result)){
                        Result ret = JsonUtils.fromJson(result,Result.class);
                        if(ret.getCode()!= ResponseCode.OPT_OK.getCode()){
                            retry(path,taskId,task);
                        }
                    }else{
                        retry(path,taskId,task);
                    }
                }catch (Exception e){
                    retry(path,taskId,task);
                }
            }else{
                logger.info("@execute restful - over retry count set retryFail status - para:{}", task);
                task.setStatus(TaskStatus.RETRYFAIL.getId());
                taskRepository.save(task);
            }
        }
    }

    private void initThisTimeTask(Task task) {
        task.setEndTime(null);
        task.setRetryCounted(0);
        task.setBeginTime(null);
        task.setStatus(TaskStatus.SEND.getId());
        task.setSendTime(new Date());
        taskRepository.save(task);
    }

    private void addLastTaskRecord(Task task) {
        TaskRecord taskRecord = new TaskRecord();
        BeanUtils.copyProperties(task, taskRecord);
        taskRecord.setTaskId(task.getId());
        taskRecord.setId(null);
        taskRecordRepository.save(taskRecord);
    }

}
