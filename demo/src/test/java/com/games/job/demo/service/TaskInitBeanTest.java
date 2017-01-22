package com.games.job.demo.service;

import java.util.HashSet;
import java.util.Set;

import com.games.job.demo.ApplicationTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.games.job.demo.model.TaskModel;
import com.games.job.demo.utils.JsonUtils;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Created by wangshichao on 2016/11/23.
 */
public class TaskInitBeanTest extends ApplicationTest{

    @Autowired
    private TaskInitBean taskInitBean;
    @Value("${spring.quartz.group}")
    private String group = "";
    @Value("${spring.quartz.jobChannel}")
    private String jobChannel = "";
    @Value("${spring.quartz.jobStatusChannel}")
    private String jobStatusChannel = "";
    @Autowired
    private ShardedJedisPool shardedJedisPool;
    private static final Logger log = LoggerFactory.getLogger(AnnotationDemo.class);

    @Test
    public void  testInitBean() throws InterruptedException {

        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        shardedJedis.del(jobChannel);
        taskInitBean.initTask();
        Set<String> stringSet = new HashSet();
        while (true) {
            String taskJson = shardedJedis.spop(jobChannel);
            if (StringUtils.isEmpty(taskJson)) {
                break;
            }
            stringSet.add(taskJson);
        }
        Assert.assertTrue(stringSet.size()==2);
        log.info(stringSet.toString());
        for(String str: stringSet){
            TaskModel taskModel = JsonUtils.fromJson(str,TaskModel.class);
            taskModel.validInitJobModel();
            if(taskModel.getBeanName().equals("annotationDemo")){
                Assert.assertTrue(taskModel.getRetryCount()==4);
            }
            if(taskModel.getBeanName().equals("AnnotationTest")){
                Assert.assertTrue(taskModel.getRetryCount()==3);
            }
        }
    }
}
