package com.games.job.common.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Created by wangshichao on 2016/11/16.
 */

@Data
public class TaskModel {

    private String taskGroup;
    private String jobName;
    private String cronExpression;
    private Date beginTime;
    private Date endTime;
    private Date createTime;
    private Integer status;
    private String beanName;
    private Integer taskId;
    private Integer retryCount;
    private Long  dealTime;
    private String path;

    public TaskModel clone() {
        return this;
    }

    public void   validInitJobModel(){

        if(StringUtils.isEmpty(this.getBeanName())){
            throw new RuntimeException("beanName is null");
        }
        if(StringUtils.isEmpty(this.getCronExpression())){
            throw new RuntimeException("cronExpression is null");
        }
        if(StringUtils.isEmpty(this.taskGroup)){
            throw new RuntimeException("taskGroup is null");
        }
        if(this.getRetryCount()==null){
            throw new RuntimeException("retryCount is null");
        }
        if(StringUtils.isEmpty(this.getJobName())){
            throw new RuntimeException("jobName is null");
        }
    }

    public void   validNotifyTaskModel(){

        if(StringUtils.isEmpty(this.getJobName())){
            throw new RuntimeException("jobName is null");
        }
        if(this.getTaskId()==null){
            throw new RuntimeException("taskId is null");
        }
    }

    public  void  validStatusMachineTaskModel(){

        if(this.getTaskId()==null){
            throw new RuntimeException("taskId is null");
        }
        if(this.getStatus()==null){
            throw new RuntimeException("status is null");
        }
    }
    public void  initDealTime(){
        this.dealTime = new Date().getTime();
    }
}
