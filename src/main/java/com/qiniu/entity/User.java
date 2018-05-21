package com.qiniu.entity;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue
    private long id;//唯一序号
    @NotEmpty(message = "姓名不能为空")
    @Column(nullable = false)
    private String userName = "";//姓名
    @NotEmpty(message = "工号不能为空")
    @Column(nullable = false, unique = true)
    private String account;//账号
    @NotEmpty(message = "密码不能为空")
//    @Length(min = 6, message = "密码长度不能小于6位")
    @Column(nullable = false)
    private String password;//密码
    //    @NotEmpty(message = "权限不能为空")
//    @Column(nullable = false)
//    private int userType;//角色类型,1管理员，2操作员
    @NotEmpty(message = "权限名不能为空")
    @Column(nullable = false)
    private String userTypeName;//角色名字

    private int quaNum;//合格数
    private int unQuaNum;//不合格数

    public StringBuilder getErrors(BindingResult result) {
        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError item : result.getAllErrors()) {
            if (!errorMsg.toString().equals("")) {
                errorMsg.append(",");
            }
            errorMsg.append(item.getDefaultMessage());
        }
        return errorMsg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public int getQuaNum() {
        return quaNum;
    }

    public void setQuaNum(int quaNum) {
        this.quaNum = quaNum;
    }

    public int getUnQuaNum() {
        return unQuaNum;
    }

    public void setUnQuaNum(int unQuaNum) {
        this.unQuaNum = unQuaNum;
    }

}
