package com.games.job.server.job;

import com.games.job.common.listener.MqListener;
import com.games.job.common.model.TaskModel;
import com.games.job.server.service.TaskService;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author:liujh
 * @create_time:2017/2/27 13:09
 * @project:job-center
 * @full_name:com.games.job.server.job.MaListener
 * @ide:IntelliJ IDEA
 */
public class MqMonitorJob extends MqListener {
    @Autowired
    private TaskService taskService;

    @KafkaListener(topics = {"quartz_job_topic"})
    public void getTask(ConsumerRecord<?, ?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            TaskModel taskModel= (TaskModel) kafkaMessage.get();
            taskService.addOrModQuartz(taskModel);
        }
    }


    @KafkaListener(topics = {"quartz_job_status_topic"})
    public void getTaskStatus(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            TaskModel taskModel= (TaskModel) kafkaMessage.get();
            taskService.modTasksStatus(Lists.newArrayList(taskModel));
        }
    }
}
