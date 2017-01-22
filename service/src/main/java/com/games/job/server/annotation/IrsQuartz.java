package com.games.job.server.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IrsQuartz {

    String jobName() default "";

    String cronExpression()  default "";

    String beanName() default "";
}