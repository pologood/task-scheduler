package com.games.job.server.entity.restful;

/**
 * @author:liujh
 * @create_time:2017/2/17 16:52
 * @project:job-center
 * @full_name:com.games.job.server.entity.restful.Result
 * @ide:IntelliJ IDEA
 */
public class Result<T> {
    public static final int OK_CODE = 200;

    private static final String OK_MSG = "OK";

    private int code = OK_CODE;

    private String message = OK_MSG;

    private T data;

    public int getCode() {
        return code;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    public Result setCodeMessage(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMsg();
        return this;
    }

    public Result setCodeMessage(ResponseCode responseCode, T data) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMsg();
        this.data = data;
        return this;
    }

}
