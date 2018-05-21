package com.device.rpc.impl;

import com.device.entity.Coordinate;
import com.device.entity.dto.CheckResultDTO;
import com.device.entity.dto.CheckResultItemDTO;
import com.device.entity.dto.CutImageDTO;
import com.device.entity.dto.TemplateDTO;
import com.device.entity.dto.WebCamResultDTO;
import com.device.rpc.CheckRpc;
import com.device.rpc.serialport;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
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
        return testCheckResult(templateDTO);
    }

    @Override
    //分割图片，输入为类型，系数值，待分割小图片；输出为图片url
    public CutImageDTO cutImage(CutImageDTO cutImageDTO) {
        if(cutImageDTO.getType() == 1)
        {
            Mat pic = Imgcodecs.imread( cutImageDTO.getCurrentImgUrl(),0);   //读取需要分割的图片，转化到灰度图加载到Mat结构变量pic，
            Imgproc.threshold(pic,pic,(int)cutImageDTO.getValue() * 255,255,Imgproc.THRESH_BINARY);  //按读取到的Value值二值化图片
            Imgcodecs.imwrite(cutImageDTO.getResultImgUrl(),pic);                 //保存到结果图片地址，供显示框显示分割结果
        }
        else
        {
            Mat pic = Imgcodecs.imread( cutImageDTO.getCurrentImgUrl(),0);   //读取需要分割的图片，转化到灰度图加载到Mat结构变量pic，
            Imgproc.threshold(pic,pic,(int)cutImageDTO.getValue() * 255,0,Imgproc.THRESH_BINARY);  //按读取到的Value值二值化图片
            Imgcodecs.imwrite(cutImageDTO.getResultImgUrl(),pic);
        }
        testCutImageResult().setType(cutImageDTO.getType());
        testCutImageResult().setValue(cutImageDTO.getValue());
        testCutImageResult().setCurrentImgUrl(cutImageDTO.getResultImgUrl());
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

    private CutImageDTO testCutImageResult()
    {
        CutImageDTO result = new CutImageDTO();
        result.setType(1);
        result.setValue(0.6f);
        result.setCurrentImgUrl("/img/1.png");
        result.setResultImgUrl("/img/2.png");

        return result;
    }

    private CheckResultDTO testCheckResult(TemplateDTO templateDTO) {
        CheckResultDTO result = new CheckResultDTO();
        result.setResult(false);

//        List<CheckResultItemDTO> rList = new ArrayList<>();
//        CheckResultItemDTO item = new CheckResultItemDTO();
//        item.setId(1);
//        item.setName(templateDTO.getRefs().get(0).getName());
//        item.setTrueImgUrl("/img/1.png");
//        item.setWrongImgUrl("/img/2.png");
//        item.setCoordinate(templateDTO.getRefs().get(0));
//        rList.add(item);
//
//        CheckResultItemDTO item1 = new CheckResultItemDTO();
//        item1.setId(2);
//        item1.setName(templateDTO.getRefs().get(3).getName());
//        item1.setTrueImgUrl("/img/3.png");
//        item1.setWrongImgUrl("/img/4.png");
//        item1.setCoordinate(templateDTO.getRefs().get(3));
//        rList.add(item1);

        List<Coordinate> a = templateDTO.getRefs();
        List<Coordinate> b = templateDTO.getRefs();
        List<CheckResultItemDTO> rList = new ArrayList<>();

        for (Coordinate it : a) {
            CheckResultItemDTO item = new CheckResultItemDTO();
            item.setId((int) it.getId());
            item.setName(it.getName());
            item.setTrueImgUrl("muban-1-false");//不需要
            item.setWrongImgUrl("muban-1-F03");//需要
            item.setCoordinate(it);
            rList.add(item);
        }

        for (Coordinate it : b) {
            CheckResultItemDTO item = new CheckResultItemDTO();
            item.setId((int) it.getId());
            item.setName(it.getName());
            item.setTrueImgUrl("/img/1.png");//不需要
            item.setWrongImgUrl("/img/2.png");//需要
            item.setCoordinate(it);
            rList.add(item);
        }

        result.setData(rList);
        return result;
    }
}