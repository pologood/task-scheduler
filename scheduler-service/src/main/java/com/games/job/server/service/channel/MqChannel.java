package com.games.job.server.service.channel;

import com.games.job.common.model.TaskModel;
import com.games.job.server.service.TaskService;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/27 11:08
 * @project:job-center
 * @full_name:com.games.job.server.service.channel.MqChannel
 * @ide:IntelliJ IDEA
 */
public class MqChannel implements Channel{

    private KafkaTemplate kafkaTemplate;

    @Override
    public Set<TaskModel> getTask() {
        return null;
    }

    @Override
    public Set<TaskModel> getTaskStatus() {
        return null;
    }

    @Override
    public void putTask(TaskModel task) {
        kafkaTemplate.send(task.getTaskGroup(),task);
    }


    public KafkaTemplate getKafkaTemplate() {
        return kafkaTemplate;
    }

    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
