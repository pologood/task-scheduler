package com.games.job.client.service.channel;

import com.games.job.common.model.TaskModel;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/24 16:20
 * @project:job-center
 * @full_name:com.games.job.client.service.channel.HttpChannel
 * @ide:IntelliJ IDEA
 */
@Component
public class RestfulChannel implements Channel{
    @Override
    public void sendTask(TaskModel taskModel) {

    }

    @Override
    public void sendTaskStatus(TaskModel taskModel) {

    }

    @Override
    public Set<TaskModel> getNotification(String group) {
        return null;
    }
}
