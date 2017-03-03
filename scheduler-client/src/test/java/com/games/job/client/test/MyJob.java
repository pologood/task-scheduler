package com.games.job.client.test;

import org.springframework.stereotype.Service;

import com.games.job.client.annotation.Quartz;
import com.games.job.client.job.Job;

/**
 * @author:liujh
 * @create_time:2017/3/1 16:32
 * @project:task-scheduler
 * @full_name:com.games.job.client.demo.MyJob
 * @ide:IntelliJ IDEA
 */
@Service
@Quartz(jobName = "myJob",cronExpression = "0 0/1 * * * ?",retryCount = 3)
public class MyJob implements Job{
    @Override
    public void run() {
        System.out.println("haha,我执行了啊~~~~");
    }
}
