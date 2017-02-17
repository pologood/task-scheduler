package com.games.job.server.service.producer;

import com.games.job.common.model.TaskModel;

/**
 * @author:liujh
 * @create_time:2017/2/17 18:47
 * @project:job-center
 * @full_name:com.games.job.server.service.producer.JobProducer
 * @ide:IntelliJ IDEA
 */
public interface JobProducer {

    /**
     * 时间到时进行任务发送
     * @param task
     */
    void  notifyTaskInstance(TaskModel task);
}
