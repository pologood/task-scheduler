package com.games.job.server.service.consumer;

import java.util.Optional;
import java.util.Set;

import com.games.job.common.constant.Constants;
import com.games.job.server.service.JobService;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.games.job.common.model.TaskModel;


@Component
public class MqJobConsumer {
    @Autowired
    private JobService jobService;


    @KafkaListener(topics = {"quartz_job_status_topic"})
    public void getTaskFromStatusChannel(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            TaskModel taskModel= (TaskModel) kafkaMessage.get();
            jobService.modTasksStatus(Lists.newArrayList(taskModel));
        }
    }

    @KafkaListener(topics = {"quartz_job_topic"})
    public void getTaskFromChannel(ConsumerRecord<?, ?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            TaskModel taskModel= (TaskModel) kafkaMessage.get();
            jobService.addOrModJob(taskModel);
        }
    }

}
