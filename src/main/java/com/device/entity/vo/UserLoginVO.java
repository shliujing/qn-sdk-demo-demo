package com.device.entity.vo;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.persistence.Column;

public class UserLoginVO {
    @NotEmpty(message = "工号不能为空")
    @Column(nullable = false, unique = true)
    private String account;//账号
    @NotEmpty(message = "密码不能为空")
//    @Length(min = 6, message = "密码长度不能小于6位")
    @Column(nullable = false)
    private String password;//密码

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
}
