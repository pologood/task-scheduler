package com.games.job.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.games.job.server.entity.Task;
import com.games.job.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge = 3600L)
@RequestMapping("/irs/quartz")
public class QuartzController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/task/delete", method = RequestMethod.DELETE)
    public Object delete(@RequestBody Integer taskId) {

        taskService.deleteJob(taskId);
        Map<String, Object> map = new HashMap<>();
        map.put("status", "ok");
        return map;
    }

    @RequestMapping(value = "/task/list", method = RequestMethod.GET)
    public List<Task>  list() {
        return  taskService.listAllTask();
    }

}
