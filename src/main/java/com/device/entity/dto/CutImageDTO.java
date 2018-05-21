package com.device.entity.dto;

/**
 * 分割图片的DTO
 */
public class CutImageDTO {
    //类型
    private int type;//模式类别1,2 todo具体是什么？
    //系数值
    private float value;
    //分割前图片路径
    private String currentImgUrl;
    //分过结果图片路径
    private String resultImgUrl;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getCurrentImgUrl() {
        return currentImgUrl;
    }

    public void setCurrentImgUrl(String currentImgUrl) {
        this.currentImgUrl = currentImgUrl;
    }

    public String getResultImgUrl() {
        return resultImgUrl;
    }

    public void setResultImgUrl(String resultImgUrl) {
        this.resultImgUrl = resultImgUrl;
    }
}
