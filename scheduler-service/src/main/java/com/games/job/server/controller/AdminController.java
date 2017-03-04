package com.games.job.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping(value = "/login")
    public String login(Model model){
        // TODO: 2017/3/4 权限校验
        return "/admin/login";
    }

    @RequestMapping(value = "/index")
    public String index(Model model){
        // TODO: 2017/3/4 菜单数据，配置读取
        return "/admin/index";
    }
}
