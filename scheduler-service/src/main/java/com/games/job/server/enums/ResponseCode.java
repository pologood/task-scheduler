
package com.games.job.server.enums;

public enum ResponseCode {
    OPT_SUCCESSFUL(1000, "successful"),
    OPT_FAILED(9999, "failed"),
    OPT_OK(200, "SUCCESS");

    private final Integer code;

    private final String msg;

    private ResponseCode(Integer code, String msg) {
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
