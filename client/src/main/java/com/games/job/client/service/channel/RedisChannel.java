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

@Component
public class RedisChannel implements Channel{

    @Value("${spring.quartz.jobChannel}")
    private String  jobChannel = "";

    @Value("${spring.quartz.jobStatusChannel}")
    private String  jobStatusChannel = "";

    @Autowired
    @Qualifier("quartzJedisPool")
    private ShardedJedisPool shardedJedisPool;

    private static final Logger log = LoggerFactory.getLogger(RedisChannel.class);

    @Override
    public void  sendTask(TaskModel taskModel){
        taskModel.validInitJobModel();
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
         try {
             shardedJedis.sadd(jobChannel, JsonUtils.toJson(taskModel));
         }catch (Exception e){
             log.error("@sendJobToChannel - add  job fail - para:{}",taskModel,e);
             throw new RuntimeException("add  job fail ");
         }finally {
             shardedJedis.close();
         }
    }

    @Override
    public  Set<TaskModel>  getNotification(String group){
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

    @Override
    public void  sendTaskStatus(TaskModel taskModel){
        taskModel.validStatusMachineTaskModel();
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        try {
            shardedJedis.sadd(jobStatusChannel, JsonUtils.toJson(taskModel));
        }catch (Exception e){
            log.error("@sendTaskMachineStatusToChannel - send  status fail - para:{}",taskModel,e);
            throw new RuntimeException("send  status fail");
        }finally {
            shardedJedis.close();
        }
    }
}
