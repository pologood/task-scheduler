package com.games.job.server.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

/**
 * @author:liujh
 * @create_time:2017/3/4 15:26
 * @project:task-scheduler
 * @full_name:com.games.job.common.async.AsyncServiceImpl
 * @ide:IntelliJ IDEA
 */
@Component
public class AsyncServiceImpl implements AsyncService {

    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    @Override
    public void executeTask(Runnable task) {
        threadPool.execute(task);
    }

    @Override
    public void submitTask(Runnable task) {
        threadPool.submit(task);
    }

    @Override
    public void shutdown() {
        threadPool.shutdown();
    }
}
