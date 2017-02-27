package com.games.job.server.service.channel;

import com.games.job.common.model.TaskModel;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/27 11:08
 * @project:job-center
 * @full_name:com.games.job.server.service.channel.RestfulChannel
 * @ide:IntelliJ IDEA
 */
public class RestfulChannel implements Channel{

    @Override
    public Set<TaskModel> getTask() {
        return null;
    }

    @Override
    public Set<TaskModel> getTaskStatus() {
        return null;
    }

    @Override
    public void putTask(TaskModel task) {

    }
}
