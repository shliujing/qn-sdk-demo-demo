package com.qiniu.service;

import com.qiniu.entity.Template;
import com.qiniu.entity.dto.CheckResultDTO;
import com.qiniu.entity.dto.TemplateDTO;
import com.qiniu.entity.Coordinate;
import com.qiniu.entity.dto.CutImageDTO;
import com.qiniu.entity.dto.WebCamResultDTO;

import java.util.List;

public interface MainService {
    public List<Template> getTemplateList();

    public Template findTemplateById(long id);

    public List<Coordinate> findCoordinateByTId(long tId);

    public long save(Template template);

    public long saveCoordinate(Coordinate coordinate);

    public void edit(Template template);

    public void delete(long id);

    public CheckResultDTO check(TemplateDTO templateDTO);

    public List<Coordinate> getCheckInfo(long id);

    public CutImageDTO cutImage(CutImageDTO cutImageDTO);

    public WebCamResultDTO getWebCam();

    public String getWebCamImage();
}
