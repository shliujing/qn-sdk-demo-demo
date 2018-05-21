package com.qiniu.entity.dto;

import com.qiniu.entity.Coordinate;
import com.qiniu.entity.Template;

import java.util.ArrayList;
import java.util.List;

public class TemplateDTO {
    private long id;//唯一主键
    private String name;//模板名称
    private String imgUrl;//模板图片地址
    private String currentImgUrl;//当前截图的图片地址 todo 考虑换成实时图像源 数据结构。只有做检测入参的时候有值
    private List<Coordinate> refs;//参考点集合
    private List<Coordinate> checks;//检测点集合

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

    public String getCurrentImgUrl() {
        return currentImgUrl;
    }

    public void setCurrentImgUrl(String currentImgUrl) {
        this.currentImgUrl = currentImgUrl;
    }

    public List<Coordinate> getRefs() {
        return refs;
    }

    public void setRefs(List<Coordinate> refs) {
        this.refs = refs;
    }

    public List<Coordinate> getChecks() {
        return checks;
    }

    public void setChecks(List<Coordinate> checks) {
        this.checks = checks;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public static TemplateDTO convertToTemplateDTO(Template templateDo, List<Coordinate> coordinateDos, TemplateDTO result) {
        if (templateDo != null) {
            result.setName(templateDo.getName());
            result.setImgUrl(templateDo.getImgUrl());
        }

//        todo valid
        List<Coordinate> refs = new ArrayList<>();
        List<Coordinate> checks = new ArrayList<>();
        if (coordinateDos != null) {
            for (Coordinate it : coordinateDos) {
                if (it.getType() == 1) {// todo add enum
                    refs.add(it);
                } else if (it.getType() == 2) {
                    checks.add(it);
                }
            }
        }

        result.setRefs(refs);
        result.setChecks(checks);

        return result;
    }
}
