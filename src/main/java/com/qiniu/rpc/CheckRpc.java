package com.qiniu.rpc;

import com.qiniu.entity.dto.CheckResultDTO;
import com.qiniu.entity.dto.CutImageDTO;
import com.qiniu.entity.dto.TemplateDTO;
import com.qiniu.entity.dto.WebCamResultDTO;

public interface CheckRpc {
    //检测，输入为 一套模板数据，原始截图；输出为 错误小图片集合（主要是错），检测结果
    public CheckResultDTO check(TemplateDTO templateDTO);

    //分割图片，输入为类型，系数值，待分割小图片；输出为图片url
    public CutImageDTO cutImage(CutImageDTO cutImageDTO);

    //获取摄像头实时数据
    public WebCamResultDTO getWebCam();

    //获取摄像头截图
    public String getWebCamImage();
}
