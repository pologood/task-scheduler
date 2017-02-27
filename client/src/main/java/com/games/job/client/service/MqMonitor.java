package com.games.job.client.service;

import com.games.job.common.constant.Constants;
import com.games.job.common.listener.MqListener;
import com.games.job.common.model.TaskModel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author:liujh
 * @create_time:2017/2/27 13:33
 * @project:job-center
 * @full_name:com.games.job.client.service.MqMonitor
 * @ide:IntelliJ IDEA
 */
@Component
public class MqMonitor extends MqListener {

    @Value("${quartz.group}")
    private String quartzGroup= Constants.TASK_GROUP_NAME;

    @KafkaListener(topics = {"quartz_job_topic"})
    public void getTask(ConsumerRecord<?, ?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            TaskModel taskModel= (TaskModel) kafkaMessage.get();
            // TODO: 2017/2/27 执行job 单独提取出来
        }
    }

}
