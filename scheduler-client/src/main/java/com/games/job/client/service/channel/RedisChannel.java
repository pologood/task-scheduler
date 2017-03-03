package com.games.job.client.service.channel;

import java.util.HashSet;
import java.util.Set;

import com.games.job.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.games.job.common.model.TaskModel;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisChannel implements Channel{

    private ShardedJedisPool shardedJedisPool;

    private String  jobChannel;
    private String  jobStatusChannel;

    private static final Logger log = LoggerFactory.getLogger(RedisChannel.class);

    @Override
    public void  putTask(TaskModel taskModel){
        taskModel.validInitJobModel();
        send(jobChannel,taskModel);
    }


    @Override
    public void  putTaskStatus(TaskModel taskModel){
        taskModel.validStatusMachineTaskModel();
        send(jobStatusChannel,taskModel);
    }

    private void send(String channel,TaskModel taskModel){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            shardedJedis.sadd(channel, JsonUtils.toJson(taskModel));
        }catch (Exception e){
            log.error("@sendTaskMachineStatusToChannel - send  status fail - para:{}",taskModel,e);
            throw new RuntimeException("send  status fail");
        }finally {
            shardedJedis.close();
        }
    }

    @Override
    public  Set<TaskModel>  getTasks(String group){
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        Set<TaskModel> set = new HashSet();
        try {
            while (true) {
                String taskJson = shardedJedis.spop(group);
                if (taskJson == null) {
                    log.info("QuartzChannelService@getNotificationFromChannel no task  from  channel :{}",group);
                    break;
                }
                set.add(JsonUtils.fromJson(taskJson,TaskModel.class));
            }
        }catch (Exception e){
            log.error("@getNotificationFromChannel - get  job fail",e);
            throw new RuntimeException("get job fail ");
        }finally {
            shardedJedis.close();
        }
        return set;
    }


    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }


    public void setJobChannel(String jobChannel) {
        this.jobChannel = jobChannel;
    }


    public void setJobStatusChannel(String jobStatusChannel) {
        this.jobStatusChannel = jobStatusChannel;
    }
}
