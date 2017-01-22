package com.games.job.server.entity;

import java.util.Date;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "qrtz_task_record")
public class TaskRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    private String taskGroup;
    private String jobName;
    private Date  beginTime;
    private Date  EndTime;
    private Date  createTime;
    private Integer taskId;
    private Integer status;
}
