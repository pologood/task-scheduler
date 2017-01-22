package com.games.job.demo.enums;

public enum TaskStatus {

    INIT(0,"未到执行时间"),
    SEND(1,"发送给task实例"),
    NOFEEDBACK(2,"task实例没有反馈"),
    BEGIN(3,"task实例开始执行"),
    END(4,"task实例已经执行结束"),
    FAIL(5,"task实例执行失败"),
    RETRYFAIL(6,"实例没有反馈重试指定次数后依然没有反馈后依然没有反馈");

    private final int id;
    private final String name;
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    TaskStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }


}
