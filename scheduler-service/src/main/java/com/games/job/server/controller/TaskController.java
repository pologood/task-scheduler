package com.games.job.server.controller;

import java.util.List;

import com.games.job.common.model.TaskModel;
import com.games.job.common.utils.JsonUtils;
import com.games.job.server.entity.Task;
import com.games.job.server.entity.TaskEmail;
import com.games.job.server.entity.TaskRecord;
import com.games.job.server.entity.VO.TaskVO;
import com.games.job.server.entity.restful.Result;
import com.games.job.server.repository.TaskEmailRepository;
import com.games.job.server.repository.TaskRecordRepository;
import com.games.job.server.service.TaskService;
import org.apache.commons.lang3.StringUtils;
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

    @RequestMapping(value = "/form",method = RequestMethod.GET)
    public void addTask(HttpServletRequest request,Model model){
        String taskId = request.getParameter("taskId");
        Task task = new Task();
        if(StringUtils.isNotBlank(taskId)){
            task = taskService.getTask(Integer.parseInt(taskId));
        }
        model.addAttribute("task",task);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public Result saveTask(String task){
        // TODO: 2017/3/4  1.cron表达式正确性校验；2.module+group+jobName唯一性校验(数据库中)；3.restful正确性校验；
        TaskModel taskModel = JsonUtils.fromJson(task,TaskModel.class);
        taskService.addOrModQuartz(taskModel);
        return new Result<String>().setData(taskModel.getJobName()+"["+taskModel.getJobGroup()+"]"+"添加成功");
    }

    @RequestMapping(value = "/del/{taskId}",method = RequestMethod.GET)
    public String delTask(@PathVariable(value = "taskId") Integer taskId) {
        taskService.delQuartz(taskId);
        return "task/list";
    }

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    public void getTask(HttpServletRequest request,Model model){
        Integer taskId = Integer.valueOf(request.getParameter("taskId"));
        List<TaskRecord> taskRecords = taskRecordRepository.findByTaskId(taskId);
        List<TaskEmail> taskEmails = taskEmailRepository.findByTaskId(taskId);
        Task task = taskService.getTask(taskId);
        TaskVO taskVO = new TaskVO();
        taskVO.setTask(task);
        taskVO.setRecords(taskRecords);
        taskVO.setEmails(taskEmails);
        model.addAttribute("taskVO",taskVO);
    }

}
