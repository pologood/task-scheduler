/*
* Copyright (c) 2015 boxfish.cn. All Rights Reserved.
*/
package com.games.job.server.example;

import com.games.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created with Intellij IDEA
 * Date: 16/2/29
 * Time: 12:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTest {
    @Test
    public void test(){
        System.out.println("我是测试的父类");
    }
}
