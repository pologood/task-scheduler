package com.games.job.server.controller;

import com.games.job.server.entity.restful.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:liujh
 * @create_time:2017/3/4 11:19
 * @project:task-scheduler
 * @full_name:com.games.job.server.controller.EmailController
 * @ide:IntelliJ IDEA
 */
@RestController
@CrossOrigin(maxAge = 3600L)
@RequestMapping("/email")
public class EmailController {


    @RequestMapping("/list")
    public Result list(){
        return new Result();
    }

    @RequestMapping("/add")
    public Result add(){
        return new Result();
    }
    @RequestMapping("/del/{emailId}")
    public Result del(@PathParam(value = "emailId") Integer emailId){

        return new Result();
    }
    @RequestMapping("/mod")
    public Result mod(){
        return new Result();
    }
}
