package com.device.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * 小图片的数据结构
 */
@Entity
public class Coordinate {
    //序号
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //类型
    @NotEmpty(message = "类型 不能为空")
    @Column(nullable = false)
    private int type;//1参考点，2检测点
    //类型
    @NotEmpty(message = "模板id 不能为空")
    @Column(nullable = false)
    private long tId;//模板id
    //名称
    @NotEmpty(message = "名称 不能为空")
    @Column(nullable = false)
    private String name;//名称
    //图片路径
    @NotEmpty(message = "图片路径 不能为空")
    @Column(nullable = false)
    private String imgUrl;//图片路径
    //分割系数值
    private float value;
    // 小图片数据结构
    @Column(nullable = false)
    private int x;//点x坐标
    @Column(nullable = false)
    private int y;//点y坐标
    @Column(nullable = false)
    private int w;//高
    @Column(nullable = false)
    private int h;//宽

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long gettId() {
        return tId;
    }

    public void settId(long tId) {
        this.tId = tId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
