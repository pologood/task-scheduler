package com.games.job.client.service.channel;

import com.games.job.common.model.TaskModel;

import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/17 17:51
 * @project:job-center
 * @full_name:com.games.job.client.service.channel.MqChannel
 * @ide:IntelliJ IDEA
 */
public class MqChannel implements Channel{
    @Override
    public void sendJobToChannel(TaskModel taskModel) {

    }

    @Override
    public Set<TaskModel> getNotificationFromChannel(String group) {
        return null;
    }

    @Override
    public void sendTaskMachineStatusToChannel(TaskModel taskModel) {

    }
}
