package com.games.job.client.annotation;

import java.lang.annotation.*;

import com.games.job.common.constant.Constants;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzRestful {

    String jobName() default "";

    String cronExpression()  default "";

    String groupName() default Constants.TASK_GROUP_NAME;

    int retryCount() default 0;

    boolean useRestful() default true;

    String url() default "";
}