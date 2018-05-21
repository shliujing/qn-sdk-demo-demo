package com.device.entity.dto;

import com.device.entity.Coordinate;

import java.util.List;

public class CheckResultDTO {

    List<CheckResultItemDTO> data;//错误图片集合
    private  Coordinate coordinate;
    private int quaNum;//合格数，在controller层处理 赋值

    private int unQuaNum;//不合格数，在controller层处理 赋值

    boolean result;//检测结果

    public List<CheckResultItemDTO> getData() {
        return data;
    }

    public void setData(List<CheckResultItemDTO> data) {
        this.data = data;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
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

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

}
