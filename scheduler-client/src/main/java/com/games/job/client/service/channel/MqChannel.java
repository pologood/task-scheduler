package com.games.job.client.service.channel;

import com.games.job.common.model.TaskModel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

/**
 * @author:liujh
 * @create_time:2017/2/17 17:51
 * @project:job-center
 * @full_name:com.games.job.client.service.channel.MqChannel
 * @ide:IntelliJ IDEA
 */
public class MqChannel implements Channel{

    private KafkaTemplate kafkaTemplate;

    private String  jobTopic;
    private String  jobStatusTopic;

    @Override
    public void putTask(TaskModel taskModel) {
        kafkaTemplate.send(jobTopic,taskModel);
    }

    @Override
    public Set<TaskModel> getTasks(String group){
        return null;
    }

    @Override
    public void putTaskStatus(TaskModel taskModel) {
        kafkaTemplate.send(jobStatusTopic,taskModel);
    }


    public String getJobTopic() {
        return jobTopic;
    }

    public void setJobTopic(String jobTopic) {
        this.jobTopic = jobTopic;
    }

    public String getJobStatusTopic() {
        return jobStatusTopic;
    }

    public void setJobStatusTopic(String jobStatusTopic) {
        this.jobStatusTopic = jobStatusTopic;
    }

    public KafkaTemplate getKafkaTemplate() {
        return kafkaTemplate;
    }

    public void setKafkaTemplate(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}
