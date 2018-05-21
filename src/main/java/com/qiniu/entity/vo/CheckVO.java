package com.qiniu.entity.vo;

/**
 * 检测返回 错误的坐标点的数据结构
 */
public class CheckVO {
    //模板序号
    private int id;

    //样图 图片路径
    private String imgUrl;//样图 图片路径

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
