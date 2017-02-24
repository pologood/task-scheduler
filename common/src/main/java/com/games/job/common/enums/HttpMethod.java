package com.games.job.common.enums;

/**
 * @author:liujh
 * @create_time:2017/2/24 17:42
 * @project:job-center
 * @full_name:com.games.job.common.enums.HttpMethod
 * @ide:IntelliJ IDEA
 */
public enum  HttpMethod {
    POST("POST"),
    GET("GET");

    private String method;

    public String getMethod() {
        return method;
    }

    private HttpMethod(String post) {
    }
}
