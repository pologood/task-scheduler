/*
* Copyright (c) 2015 boxfish.cn. All Rights Reserved.
*/
package com.games.job.server;

import org.jdto.DTOBinder;
import org.jdto.spring.SpringDTOBinder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created with Intellij IDEA
 * Date: 16/2/29
 * Time: 11:43
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public DTOBinder dtoBinder() {
        return new SpringDTOBinder();
    }
}
