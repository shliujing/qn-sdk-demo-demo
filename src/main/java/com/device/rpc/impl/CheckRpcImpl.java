package com.device.rpc.impl;

import com.device.entity.dto.CheckResultDTO;
import com.device.entity.dto.CheckResultItemDTO;
import com.device.entity.dto.CutImageDTO;
import com.device.entity.dto.TemplateDTO;
import com.device.entity.dto.WebCamResultDTO;
import com.device.rpc.CheckRpc;
import com.device.rpc.serialport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CheckRpcImpl implements CheckRpc {

    @Autowired
    serialport serialport;
	// 可删除，main函数不影响流程
    public static void main(String[] args) {
        CheckRpcImpl it = new CheckRpcImpl();
        CheckResultDTO result = it.check(null);
        System.out.println(result.isResult());
    }
    @Override
    //检测，输入为 一套模板数据，原始截图；输出为 错误小图片集合（主要是错），检测结果
    public CheckResultDTO check(TemplateDTO templateDTO) {
        // 循环获取截图的检测点图片、对比对应模板图检测点


        // 构造返回体

        //    return null;

        // 测试返回结果 待删除 todo
        return testCheckResult();
    }

    @Override
    //分割图片，输入为类型，系数值，待分割小图片；输出为图片url
    public CutImageDTO cutImage(CutImageDTO cutImageDTO) {
        // 分割图片处理

        // 结果图片存储到指定路径

        //    return null;

        // 测试返回结果 待删除 todo
        return testCutImageResult();
    }

    //获取摄像头实时数据
    @Override
    public WebCamResultDTO getWebCam() {
        return null;
    }

    //获取摄像头截图
    @Override
    public String getWebCamImage() {
        return null;
    }

    private CutImageDTO testCutImageResult() {
        CutImageDTO result = new CutImageDTO();
        result.setType(1);
        result.setValue(0.6f);
        result.setCurrentImgUrl("/img/1.png");
        result.setResultImgUrl("/img/2.png");

        return result;
    }

    private CheckResultDTO testCheckResult() {
        CheckResultDTO result = new CheckResultDTO();
        result.setResult(false);

        List<CheckResultItemDTO> rList = new ArrayList<>();
        CheckResultItemDTO item = new CheckResultItemDTO();
        item.setId(1);
        item.setName("F01");
        item.setTrueImgUrl("/img/1.png");
        item.setWrongImgUrl("/img/2.png");

        rList.add(item);
        result.setData(rList);
        return result;
    }
}