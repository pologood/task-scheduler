package com.games.job.client.service.channel;

import com.games.job.common.model.TaskModel;

import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/17 17:55
 * @project:job-center
 * @full_name:com.games.job.client.service.channel.Channel
 * @ide:IntelliJ IDEA
 */
public interface Channel {

    void  sendJobToChannel(TaskModel taskModel);

    Set<TaskModel> getNotificationFromChannel(String group);

    void  sendTaskMachineStatusToChannel(TaskModel taskModel);
}
