package com.games.job.demo.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IrsQuartz {

    String jobName() default "";

    String cronExpression()  default "";

    int retryCount()  default 0;
}
