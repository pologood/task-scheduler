package com.games.job.server.controller;

import com.games.job.common.enums.TaskStatus;
import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;
import com.games.job.server.entity.restful.Result;
import com.games.job.server.service.TaskService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:liujh
 * @create_time:2017/2/25 11:14
 * @project:job-center
 * @full_name:com.games.job.server.controller.TaskConsumerController
 * @ide:IntelliJ IDEA
 */
@RestController
@RequestMapping("/client")
public class TaskReceiveController {
    @Autowired
    private TaskService taskService;

    /**
     * 接收task instance从http通道上报的任务
     * @param data
     * @return
     */
    @RequestMapping("/getTask")
    public Result getTask(String data){
        TaskModel taskModel = JsonUtils.fromJson(data, TaskModel.class);
        if(TaskStatus.INIT.getId()==taskModel.getStatus()){
            taskService.addOrModQuartz(taskModel);
        }else{
            taskService.modTasksStatus(Lists.newArrayList(taskModel));
        }
        return new Result();
    }
}
