package com.games.job.server.controller;

import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;
import com.games.job.server.entity.restful.Result;
import com.games.job.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;

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


    @RequestMapping("/getTask")
    public Result getTask(@QueryParam(value = "data") String data){
        TaskModel taskModel = JsonUtils.fromJson(data, TaskModel.class);
        taskService.addOrModQuartz(taskModel);
        return new Result();
    }
}
