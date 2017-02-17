
package com.games.job.server.entity.restful;

public enum ResponseCode {
    OPT_SUCCESSFUL(1000, "successful"), OPT_FAILED(9999, "failed");

    private final Integer code;

    private final String msg;

    ResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
