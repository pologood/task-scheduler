package com.games.job.server.controller;

import com.games.job.server.entity.restful.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:liujh
 * @create_time:2017/2/24 13:59
 * @project:job-center
 * @full_name:com.games.job.server.controller.MainController
 * @ide:IntelliJ IDEA
 */
@RestController
@CrossOrigin(maxAge = 3600L)
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(value = "/login")
    public Result login(){
        // TODO: 2017/3/4 权限校验
        return new Result();
    }

    @RequestMapping(value = "/index")
    public Result index(){
        // TODO: 2017/3/4 菜单数据，配置读取
        return new Result();
    }
}
