package com.games.job.server.service.channel;

import com.games.job.common.model.TaskModel;

import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/27 11:07
 * @project:job-center
 * @full_name:com.games.job.server.service.channel.Channel
 * @ide:IntelliJ IDEA
 */
public interface Channel {

    Set<TaskModel> getTask();

    Set<TaskModel> getTaskStatus();

    void  putTask(TaskModel task);
}
