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
import com.device.utils.ZplPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        Coordinate coordinateDo = coordinateRepository.saveAndFlush(coordinate);
        return coordinateDo.getId();
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
/*- 获取ImageInfo.properties  sxf 20180427 start-*/
    @Override
    public Map<String,String> getImageInfoValue(String file,String imagestr) {
        ResourceBundle prop = null;
        prop = ResourceBundle.getBundle(file);
        Map<String,String> ImageInfo=new HashMap<String,String>();
        ImageInfo.put("x",prop.getString(imagestr+".x"));
        ImageInfo.put("y",prop.getString(imagestr+".y"));
        ImageInfo.put("width",prop.getString(imagestr+".width"));
        ImageInfo.put("height",prop.getString(imagestr+".height"));
        ImageInfo.put("valuej",prop.getString(imagestr+".valuej"));
        ImageInfo.put("pathj",prop.getString(imagestr+".pathj"));
        ImageInfo.put("pathg",prop.getString(imagestr+".pathg"));
        ImageInfo.put("type",prop.getString(imagestr+".type"));
        return ImageInfo;
    }
    /*- 获取ImageInfo.properties  sxf 20180427 end-*/
    /*- 获取printerInfo.properties  sxf 20180427 start-*/
    @Override
    public void labelPrint(String code) {
        // 获取资源文件中打印机驱动路径、点阵文件
        ResourceBundle prop = null;
        prop = ResourceBundle.getBundle("printerInfo");
        String printerURI=prop.getString("printerURI");
        String path=prop.getString("path");
        // 获取打印机驱动路径
        ZplPrinter p = null;
        try {
            p = new ZplPrinter(printerURI,path);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 设置日期格式
        String currentDate = df.format(new Date());
        //文本位置
       int x =Integer.parseInt( prop.getString("setText.x"));
       int y =Integer.parseInt( prop.getString("setText.y"));
       int eh=Integer.parseInt( prop.getString("setText.eh"));
       int ew=Integer.parseInt( prop.getString("setText.ew"));
       int es=Integer.parseInt( prop.getString("setText.es"));
       int mx=Integer.parseInt( prop.getString("setText.mx"));
        int my=Integer.parseInt( prop.getString("setText.my"));
        int ms=Integer.parseInt( prop.getString("setText.ms"));
       //二维码位置
        int xx=Integer.parseInt( prop.getString("setCode.xx"));
        int yy=Integer.parseInt( prop.getString("setCode.yy"));
        int hh=Integer.parseInt( prop.getString("setCode.hh"));
        int ww=Integer.parseInt( prop.getString("setCode.ww"));
        p.setText("保险丝PASS", x, y, eh, ew, es, mx, my, ms);
        // p.setCode("0123456789ABCD" + " " + currentDate, 260, 50, 2, 4);
        p.setCode(code + " " + currentDate, xx, yy, hh, ww);
        String zpl = p.getZpl();
        p.print(zpl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /*- 获取printerInfo.properties  sxf 20180427 end-*/
}


