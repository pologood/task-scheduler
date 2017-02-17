package com.games.job.client.annotation;

import com.games.job.common.constant.Constants;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Quartz {

    String jobName() default "";

    String cronExpression()  default "";

    String groupName() default Constants.TASK_GROUP_NAME;

    int retryCount() default 0;
}