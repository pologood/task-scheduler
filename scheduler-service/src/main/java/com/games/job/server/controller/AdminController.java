package com.games.job.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author:liujh
 * @create_time:2017/2/24 13:59
 * @project:job-center
 * @full_name:com.games.job.server.controller.MainController
 * @ide:IntelliJ IDEA
 */
@Controller
@CrossOrigin(maxAge = 3600L)
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public void login(Model model){
        // TODO: 2017/3/4 登录权限校验
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public void index(Model model){
        // TODO: 2017/3/4 菜单配置读取，菜单操作权限校验
    }

    @RequestMapping(value = "/home",method = RequestMethod.GET)
    public void home(Model model){

    }
}
