package com.games.job.server.async;

/**
 * @author:liujh
 * @create_time:2017/3/4 15:26
 * @project:task-scheduler
 * @full_name:com.games.job.common.async.AsyncService
 * @ide:IntelliJ IDEA
 */
public interface AsyncService {

    void executeTask(Runnable task);

    void submitTask(Runnable task);

    void shutdown();
}
