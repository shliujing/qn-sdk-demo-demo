package com.qiniu.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 查看检测日志
 * todo db字段都需要加上时间，创建和修改
 *
 */
@Entity
public class CheckLog {
    @Id
    @GeneratedValue
    private long id;//唯一主键

    @NotEmpty(message = "模板id 不能为空")
    @Column(nullable = false, unique = true)
    private String tId;//模板id

    @NotEmpty(message = "操作员账号 不能为空")
    @Column(nullable = false)
    private String account;//操作员账号

    // String对应类型为 CheckLogVo 的json
    @NotEmpty(message = "检测信息 不能为空")
    @Column(nullable = false)
    private String info;//检测信息，{}结构

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
