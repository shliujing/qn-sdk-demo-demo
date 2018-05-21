package com.qiniu.entity.dto;

public class WebCamResultDTO {
    private String imgUrl;//字段待定

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public WebCamResultDTO(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public WebCamResultDTO() {
    }
}
