package com.games.job.client.service;

import com.games.job.common.listener.MqListener;
import com.games.job.common.model.TaskModel;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author:liujh
 * @create_time:2017/2/27 13:33
 * @project:job-center
 * @full_name:com.games.job.client.service.MqMonitor
 * @ide:IntelliJ IDEA
 */
@Component
public class MqMonitor extends MqListener {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private TaskManager taskManager;

    @Value("${spring.quartz.threadCount}")
    private int threadCount = 5;

    private ExecutorService executorService;

    public MqMonitor(){
        executorService = Executors.newFixedThreadPool(threadCount);
    }


    @KafkaListener(topics = {"quartz_job_topic"})
    public void getTask(ConsumerRecord<?, ?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            TaskModel taskModel= (TaskModel) kafkaMessage.get();
            executorService.execute(new JobHandler(taskModel,applicationContext,taskManager));
        }
    }

}
