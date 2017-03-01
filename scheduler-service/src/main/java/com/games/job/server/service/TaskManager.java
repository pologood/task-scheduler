package com.games.job.server.service;

import com.games.job.common.model.TaskModel;
import com.games.job.server.service.channel.Channel;

import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/27 11:18
 * @project:job-center
 * @full_name:com.games.job.server.service.TaskReceiver
 * @ide:IntelliJ IDEA
 */
public class TaskManager {

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Set<TaskModel> receiveTask(){
        return channel.getTask();
    }

    public Set<TaskModel> receiveTaskStatus(){
        return channel.getTaskStatus();
    }

    public void sendTask(TaskModel taskModel){
        channel.putTask(taskModel);
    }
}
