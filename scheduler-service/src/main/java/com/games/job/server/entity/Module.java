package com.games.job.server.entity;

import javax.persistence.*;

/**
 * Created by 建会 on 2017/5/1.
 */

@Entity
@Table(name = "qrtz_task_module")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String moduleName;
    private String owner;
    private String ownerPhone;
    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
