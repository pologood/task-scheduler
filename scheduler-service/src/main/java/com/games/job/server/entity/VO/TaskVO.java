package com.games.job.server.entity.VO;

import java.util.List;


import com.games.job.server.entity.Task;
import com.games.job.server.entity.TaskRecord;
import com.games.job.server.entity.restful.BaseModel;


public class TaskVO extends BaseModel{
    private Task task;
    private List<TaskRecord> records;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<TaskRecord> getRecords() {
        return records;
    }

    public void setRecords(List<TaskRecord> records) {
        this.records = records;
    }
}

