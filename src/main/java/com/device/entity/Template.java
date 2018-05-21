package com.device.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;//唯一主键

    @NotEmpty(message = "模板名称 不能为空")
    @Column(nullable = false, unique = true)
    private String name;//模板名称

    @NotEmpty(message = "模板图片地址 不能为空")
    @Column(nullable = false)
    private String imgUrl;//模板图片地址

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
