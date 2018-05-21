package com.device.entity;

import com.device.common.Const;

public class BaseResult<T> {

    T obj;
    String desc;
    boolean result;
    int code;//0成功，1失败

    public BaseResult() {
    }

    public BaseResult(boolean result) {
        this.result = result;
        this.setDesc(result);
    }

    public BaseResult(T obj, boolean result) {
        this.obj = obj;
        this.result = result;
        this.setDesc(result);
    }

    public BaseResult(String desc, boolean result) {
        this.desc = desc;
        this.result = result;
        this.code = result ? Const.successCode : Const.errorCode;
    }

    private void setDesc(boolean result) {
        if (result) {
            this.desc = Const.successDesc;
            this.code = Const.successCode;
        } else {
            this.desc = Const.errorDesc;
            this.code = Const.errorCode;
        }
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
