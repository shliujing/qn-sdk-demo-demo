package com.device.service.impl;

import com.device.entity.Coordinate;
import com.device.entity.Template;
import com.device.entity.dto.CheckResultDTO;
import com.device.entity.dto.CutImageDTO;
import com.device.entity.dto.TemplateDTO;
import com.device.entity.dto.WebCamResultDTO;
import com.device.repository.CoordinateRepository;
import com.device.repository.TemplateRepository;
import com.device.rpc.CheckRpc;
import com.device.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainServiceImpl implements MainService {
    @Autowired
    TemplateRepository templateRepository;
    @Autowired
    CoordinateRepository coordinateRepository;
    @Autowired
    CheckRpc checkRpc;

    @Override
    public List<Template> getTemplateList() {
        return templateRepository.findAll();
    }

    @Override
    public Template findTemplateById(long id) {
        return templateRepository.findById(id);
    }

    @Override
    public List<Coordinate> findCoordinateByTId(long tId) {
        return coordinateRepository.findAllByTId(tId);
    }

    //todo valid 有可能是直接在原对象上赋值
    @Override
    public long save(Template template) {
        Template templateDo = templateRepository.saveAndFlush(template);
        return templateDo.getId();
    }

    @Override
    public long saveCoordinate(Coordinate coordinate) {
        return 0;
        //todo
//        Coordinate coordinateDo = coordinateRepository.saveAndFlush(coordinate);
//        return coordinateDo.getId();
    }

    @Override
    public void edit(Template template) {
        templateRepository.save(template);
    }

    @Override
    public void delete(long id) {
        templateRepository.delete(id);
    }

    @Override
    public CheckResultDTO check(TemplateDTO templateDTO) {
        return checkRpc.check(templateDTO);
    }

    @Override
    public List<Coordinate> getCheckInfo(long id) {
        return null;
    }

    @Override
    public CutImageDTO cutImage(CutImageDTO cutImageDTO) {
        return checkRpc.cutImage(cutImageDTO);
    }

    //todo 是C++端提供摄像头实时信息吗（我也可以展示摄像头实时），如果是c++提供，则还需要一个截取摄像头的函数（新建模板）
    @Override
    public WebCamResultDTO getWebCam() {
        return checkRpc.getWebCam();
    }

    @Override
    public String getWebCamImage() {
        return checkRpc.getWebCamImage();
    }
}


