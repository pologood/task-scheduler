package com.games.job.common.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * Created by wangshichao on 2016/11/16.
 */

public class TaskModel{

    private String jobGroup;
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
    private String failReason;
    private Boolean fail=false;

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
        if(StringUtils.isEmpty(this.jobGroup)){
            throw new RuntimeException("jobGroup is null");
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Long getDealTime() {
        return dealTime;
    }

    public void setDealTime(Long dealTime) {
        this.dealTime = dealTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Boolean getFail() {
        return fail;
    }

    public void setFail(Boolean fail) {
        this.fail = fail;
    }
}
