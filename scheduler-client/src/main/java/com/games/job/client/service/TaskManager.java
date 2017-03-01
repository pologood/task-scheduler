package com.games.job.client.service;


import com.games.job.client.service.channel.Channel;
import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/17 18:13
 * @project:job-center
 * @full_name:com.games.job.client.service.reporter.ReporterAdapter
 * @ide:IntelliJ IDEA
 */
public class TaskManager {

    private Channel channel;

    public void sendTasks(List<TaskModel> taskModels){
        for(TaskModel taskModel:taskModels){
            channel.putTask(taskModel);
        }
    }

    public Set<TaskModel> receiveTasks(String group){
        return channel.getTasks(group);
    }

    public void sendBeginStatus(TaskModel taskModel) {
        TaskModel beginTaskModel = taskModel.clone();
        beginTaskModel.setStatus(TaskStatus.BEGIN.getId());
        beginTaskModel.setBeginTime(new Date());
        beginTaskModel.initDealTime();
        channel.putTaskStatus(beginTaskModel);
    }

    public void sendEndStatus(TaskModel taskModel) {
        TaskModel endTaskModel = taskModel.clone();
        endTaskModel.setEndTime(new Date());
        endTaskModel.setStatus(TaskStatus.END.getId());
        endTaskModel.initDealTime();
        channel.putTaskStatus(endTaskModel);
    }

    public void sendFailStatus(TaskModel taskModel) {
        TaskModel failTaskModel = taskModel.clone();
        failTaskModel.setStatus(TaskStatus.FAIL.getId());
        failTaskModel.setEndTime(new Date());
        failTaskModel.initDealTime();
        channel.putTaskStatus(failTaskModel);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
