package com.games.job.client.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Quartz {

    String jobName() default "";

    String cronExpression()  default "";

    int retryCount() default 0;

}