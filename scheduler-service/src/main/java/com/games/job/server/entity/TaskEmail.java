package com.games.job.server.entity;

import javax.persistence.*;

import com.games.job.server.entity.restful.BaseModel;

import lombok.Data;

@Data
@Entity
@Table(name = "qrtz_task_email")
public class TaskEmail extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    private String taskGroup;
    private String jobName;
    private Integer taskId;
    private String name;
    private String deptName;
    private String emailAddress;
    private String phone;
    private Boolean valid;
}
