package com.games.job.server.example;

import com.games.job.client.service.TaskManager;
import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wangshichao on 2016/11/23.
 */
public class TaskTest extends ApplicationTest {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private TaskManager taskManager;

    @Value("${spring.quartz.group}")
    private String group;

    @Value("${spring.redis.jobChannel}")
    private String jobChannel;

    @Value("${spring.redis.jobStatusChannel}")
    private String jobStatusChannel;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Test
    public void  test_taskInit() throws InterruptedException {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        shardedJedis.del(jobChannel);
        List<TaskModel> taskModels = Lists.newArrayList();
        for(int i=0;i<2;i++){
            TaskModel taskModel = new TaskModel();
            taskModel.setTaskGroup("service");
            taskModel.setJobName("testJob"+i);
            if(i==1){
                taskModel.setBeanName("annotationDemo");
                taskModel.setRetryCount(4);
            }else{
                taskModel.setBeanName("AnnotationTest");
                taskModel.setRetryCount(3);
            }
            taskModel.setStatus(TaskStatus.INIT.getId());
            taskModel.setCreateTime(new Date());
            taskModel.setCronExpression("000 /99");
            taskModels.add(taskModel);
        }
        taskManager.sendTasks(taskModels);
        Set<String> stringSet = new HashSet();
        while (true) {
            String taskJson = shardedJedis.spop(jobChannel);
            if (StringUtils.isEmpty(taskJson)) {
                break;
            }
            stringSet.add(taskJson);
        }
        Assert.assertTrue(stringSet.size()==2);
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

    @Test
    public void test_taskProcess() {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        shardedJedis.del(jobStatusChannel);

        for(int i=0;i<3;i++){
            TaskModel taskModel = new TaskModel();
            taskModel.setTaskId(23);
            taskModel.setTaskGroup(group);
            taskModel.setJobName("jobNameTest");
            taskModel.setBeanName("annotationDemo");
            taskModel.setCronExpression("xxoo");
            taskModel.setRetryCount(i);
            if(i==1){
                taskManager.sendBeginStatus(taskModel);
            }else if(i==2){
                taskManager.sendEndStatus(taskModel);
            }else{
                taskManager.sendFailStatus(taskModel);
            }

        }
        Set<String> stringSet = new HashSet<>();
        while (true) {
            String taskJson = shardedJedis.spop(jobStatusChannel);
            if (taskJson == null) {
                break;
            }
            stringSet.add(taskJson);
        }
        Assert.assertTrue(stringSet.size() == 3);

        Set<Integer> statusSet = new HashSet<>();
        for (String str : stringSet) {
            TaskModel taskModel1 = JsonUtils.fromJson(str, TaskModel.class);
            taskModel1.validStatusMachineTaskModel();
            if (taskModel1.getStatus().intValue() == TaskStatus.BEGIN.getId()) {
                statusSet.add(taskModel1.getStatus());
                Assert.assertTrue(taskModel1.getBeginTime() != null);
            } else if (taskModel1.getStatus().intValue() == TaskStatus.END.getId()) {
                statusSet.add(taskModel1.getStatus());
                Assert.assertTrue(taskModel1.getEndTime() != null);
            } else if(taskModel1.getStatus().intValue() == TaskStatus.FAIL.getId()){
                statusSet.add(taskModel1.getStatus());
            }
        }
        Assert.assertTrue(statusSet.size() == 3);
    }
}
