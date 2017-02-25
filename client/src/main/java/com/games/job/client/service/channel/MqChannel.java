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
@Component
public class MqChannel implements Channel{

    @Value("${spring.kafka.jobTopic}")
    private String  jobTopic = "quartz_job_topic";

    @Value("${spring.kafka.jobStatusTopic}")
    private String  jobStatusTopic = "quartz_job_status_topic";

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public void sendTask(TaskModel taskModel) {
        kafkaTemplate.send(jobTopic,taskModel);
    }



    @Override
    public Set<TaskModel> getNotification(String group){
        return null;
    }

    @Override
    public void sendTaskStatus(TaskModel taskModel) {
        kafkaTemplate.send(jobStatusTopic,taskModel);
    }
}
