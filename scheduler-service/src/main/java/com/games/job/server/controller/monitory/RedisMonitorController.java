package com.games.job.server.controller.monitory;

import com.games.job.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Properties;

/**
 * @author:liujh
 * @create_time:2017/2/24 16:45
 * @project:job-center
 * @full_name:com.games.job.server.controller.RedisMonitorController
 * @ide:IntelliJ IDEA
 *
 * redis性能监控
 */
@Controller
@RequestMapping("/monitor")
public class RedisMonitorController {

    @Autowired
    private StringRedisTemplate template;

    @RequestMapping(value = "/redis/info",method = RequestMethod.GET)
    public void redisInfo(Model model){
        template.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) {
                Properties properties = connection.info();
                model.addAttribute("redisInfo",properties);
                return true;
            }
        });
    }
}
