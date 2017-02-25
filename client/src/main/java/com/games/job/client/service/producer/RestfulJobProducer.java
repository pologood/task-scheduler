package com.games.job.client.service.producer;

import com.games.job.client.service.channel.RestfulChannel;
import com.games.job.common.model.TaskModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author:liujh
 * @create_time:2017/2/17 17:52
 * @project:job-center
 * @full_name:com.games.job.client.service.reporter.HttpReporter
 * @ide:IntelliJ IDEA
 */
@Component
public class RestfulJobProducer extends JobProducer {
    @Autowired
    private RestfulChannel httpChannel;

    @Override
    public void sendJob(TaskModel taskModel) {
        httpChannel.sendTask(taskModel);
    }
}
