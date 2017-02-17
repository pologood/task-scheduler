package com.games.job.server.controller;

import java.util.List;

import com.games.job.server.entity.Task;
import com.games.job.server.entity.restful.Result;
import com.games.job.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@CrossOrigin(maxAge = 3600L)
@RequestMapping("/quartz")
public class QuartzController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/task/delete/{taskId}")
    public Result delTask(@PathParam(value = "taskId") Integer taskId) {
        taskService.deleteJob(taskId);
        return new Result();
    }

    @RequestMapping(value = "/task/list", method = RequestMethod.GET)
    public Result<List<Task>>  getTasks() {
        Result<List<Task>> result = new Result<>();
        result.setData(taskService.listAllTask());
        return result;
    }

}
