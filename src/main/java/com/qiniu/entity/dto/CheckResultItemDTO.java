package com.qiniu.entity.dto;

/**
 * 检测返回 错误的坐标点的数据结构
 */
public class CheckResultItemDTO {
    //正确图片id
    private int id;
    //正确图片名称
    private String name;
    //正确图片url
    private String trueImgUrl;
    //错误图片url
    private String wrongImgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrueImgUrl() {
        return trueImgUrl;
    }

    public void setTrueImgUrl(String trueImgUrl) {
        this.trueImgUrl = trueImgUrl;
    }

    public String getWrongImgUrl() {
        return wrongImgUrl;
    }

    public void setWrongImgUrl(String wrongImgUrl) {
        this.wrongImgUrl = wrongImgUrl;
    }
}
