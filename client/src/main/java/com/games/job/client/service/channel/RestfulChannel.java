package com.games.job.client.service.channel;

import com.games.job.common.enums.HttpMethod;
import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;
import com.games.job.common.utils.NetUtils;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(RestfulChannel.class);
    @Value("${job.server}")
    private String serverPath;

    @Override
    public void sendTask(TaskModel taskModel) {
        send(taskModel);
    }

    @Override
    public void sendTaskStatus(TaskModel taskModel) {
        send(taskModel);
    }

    private void send(TaskModel taskModel){
        String json = JsonUtils.toJson(taskModel);
        Map<String,String> map = Maps.newHashMap();
        map.put("data",json);
        LOGGER.info("RestfulChannel#send ,param={}",map);
        String result = NetUtils.sendMap(serverPath,map, HttpMethod.GET);
        LOGGER.info("RestfulChannel#send ,result={}",result);
    }

    @Override
    public Set<TaskModel> getNotification(String group) {
        return null;
    }
}
