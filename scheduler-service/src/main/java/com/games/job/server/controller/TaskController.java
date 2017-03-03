package com.games.job.server.controller;

import java.util.List;

import com.games.job.server.entity.Task;
import com.games.job.server.entity.TaskRecord;
import com.games.job.server.entity.VO.TaskVO;
import com.games.job.server.entity.restful.Result;
import com.games.job.server.repository.TaskRecordRepository;
import com.games.job.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@CrossOrigin(maxAge = 3600L)
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;


    @Autowired
    private TaskRecordRepository taskRecordRepository;

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public Result addTask(){
        return null;
    }

    @RequestMapping(value = "/del/{taskId}",method = RequestMethod.GET)
    public Result delTask(@PathParam(value = "taskId") Integer taskId) {
        taskService.delQuartz(taskId);
        return new Result();
    }

    @RequestMapping(value = "/mod/{taskId}",method = RequestMethod.GET)
    public Result modJob(@PathParam(value = "taskId") Integer taskId){
        return null;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<List<Task>>  getTasks() {
        Result<List<Task>> result = new Result<>();
        result.setData(taskService.getTasks());
        return result;
    }

    @RequestMapping(value = "/get/{taskId}",method = RequestMethod.GET)
    public Result<TaskVO> getTask(@PathParam(value = "taskId") Integer taskId){
        List<TaskRecord> taskRecords = taskRecordRepository.findByTaskId(taskId);
        Task task = taskService.getTask(taskId);
        TaskVO taskVO = new TaskVO();
        taskVO.setTask(task);
        taskVO.setRecords(taskRecords);
        return new Result<>().setData(taskVO);
    }

}
