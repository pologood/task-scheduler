package com.games.job.server.entity;

import java.util.Date;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "qrtz_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    private String taskGroup;
    private String jobName;
    private String cronExpression;
    private Date beginTime;
    private Date  endTime;
    private Date  createTime;
    private Integer status;
    private Integer  retryCount;
    private Integer  retryCounted;
    private Date  sendTime;
    private String beanName;
}

