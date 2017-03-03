package com.games.job.server.entity;

import java.util.Date;

import com.games.job.server.entity.restful.BaseModel;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "qrtz_task_record")
public class TaskRecord extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    private String taskGroup;
    private String jobName;
    private Date  createTime;
    private Date  beginTime;
    private Date  endTime;
    private Integer taskId;
    private Integer status;
}
