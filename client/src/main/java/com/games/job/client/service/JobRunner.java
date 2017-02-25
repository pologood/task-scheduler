package com.games.job.client.service;

import com.games.job.client.service.channel.Channel;
import com.games.job.common.model.TaskModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/17 16:27
 * @project:job-center
 * @full_name:com.games.job.client.service.DefaultJobProcessor
 * @ide:IntelliJ IDEA
 */
public class JobRunner{

    private final static Logger log = LoggerFactory.getLogger(JobRunner.class);

    private Channel channel;

    public Set<TaskModel>  getNotification(String group){
        return channel.getNotification(group);
    }

}
