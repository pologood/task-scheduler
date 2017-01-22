/*
* Copyright (c) 2015 boxfish.cn. All Rights Reserved.
*/
package com.games.job.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with Intellij IDEA
 * Date: 16/2/29
 * Time: 12:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
public class ApplicationTest {

    @Test
    public void test(){
        System.out.println("我是测试的父类");
    }
}
