package com.games.job.server.controller;

import java.util.List;

import com.games.job.server.entity.Task;
import com.games.job.server.entity.TaskEmail;
import com.games.job.server.entity.TaskRecord;
import com.games.job.server.entity.VO.TaskVO;
import com.games.job.server.entity.restful.Result;
import com.games.job.server.repository.TaskEmailRepository;
import com.games.job.server.repository.TaskRecordRepository;
import com.games.job.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin(maxAge = 3600L)
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRecordRepository taskRecordRepository;

    @Autowired
    private TaskEmailRepository taskEmailRepository;


    @RequestMapping(value = "/list")
    public void getTasks(Model model) {
        // TODO: 2017/3/4 分页、查询
        List<Task> tasks =  taskService.getTasks();
        model.addAttribute("tasks",tasks);
    }

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public void addTask(Model model){
    }

    @RequestMapping(value = "/save",method = RequestMethod.GET)
    @ResponseBody
    public Result saveTask(HttpServletRequest request){
        System.out.println("TaskController#saveTask:"+request.getParameter("jobGroup"));
        // TODO: 2017/3/4  1.cron表达式正确性校验；2.group+jobName唯一性校验；3.restful正确性校验；
        return new Result();
    }



    @RequestMapping(value = "/del/{taskId}",method = RequestMethod.GET)
    public Result delTask(@PathVariable(value = "taskId") Integer taskId) {
        taskService.delQuartz(taskId);
        return new Result();
    }

    @RequestMapping(value = "/mod/{taskId}",method = RequestMethod.GET)
    public Result modJob(@PathVariable(value = "taskId") Integer taskId){
        // TODO: 2017/3/4  1.cron表达式正确性校验；2.group+jobName唯一性校验；3.restful正确性校验；
        return new Result();
    }

    @RequestMapping(value = "/get/{taskId}",method = RequestMethod.GET)
    public Result<TaskVO> getTask(@PathVariable(value = "taskId") Integer taskId){
        List<TaskRecord> taskRecords = taskRecordRepository.findByTaskId(taskId);
        List<TaskEmail> taskEmails = taskEmailRepository.findByTaskId(taskId);
        Task task = taskService.getTask(taskId);
        TaskVO taskVO = new TaskVO();
        taskVO.setTask(task);
        taskVO.setRecords(taskRecords);
        taskVO.setEmails(taskEmails);
        return new Result<>().setData(taskVO);
    }

}
