package com.games.job.client.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QuartzRestful {

    String jobName() default "";

    String cronExpression();


    int retryCount() default 0;

    boolean useRestful() default true;

    String url();
}