package com.games.job.client.service.channel;

import com.games.job.common.model.TaskModel;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/17 17:51
 * @project:job-center
 * @full_name:com.games.job.client.service.channel.MqChannel
 * @ide:IntelliJ IDEA
 */
@Component
public class MqChannel implements Channel{
    @Override
    public void sendTask(TaskModel taskModel) {

    }

    @Override
    public Set<TaskModel> getNotification(String group) {
        return null;
    }

    @Override
    public void sendTaskStatus(TaskModel taskModel) {

    }
}
