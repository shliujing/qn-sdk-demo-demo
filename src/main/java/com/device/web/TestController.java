package com.device.web;

import com.device.entity.BaseResult;
import com.device.entity.dto.WebCamResultDTO;
import com.device.service.MainService;
import com.device.utils.ImageUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 管理员/操作员
 * 原则：尽量不传图片（传图片url），尽量java端做数据存储，c++只做算法和下位机通信模块
 */
@RequestMapping("main")
@Controller
public class TestController {
    @Resource
    MainService mainService;
    @Resource
    ImageUtils imageUtils;


    //region --------------------------测试--------------------------
    //    生成截图
    @RequestMapping("/testTestWebCam")
    @ResponseBody
    public BaseResult<WebCamResultDTO> testTestWebCam(HttpServletRequest request) {
        WebCamResultDTO result = null;
        String fileName = "";
        String filePath = imageUtils.getGalleryPath();

        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String imgName = now.format(formatter);
            fileName = imgName + ".jpeg";

            String imgStr = request.getParameter("image");

            if (null != imgStr) {
                imgStr = imgStr.substring(imgStr.indexOf(",") + 1);
            }

            Boolean flag = imageUtils.generateImage(imgStr, filePath, fileName);
            if (!flag) {
                return new BaseResult<>("图片生成异常", false);
            }
            result = new WebCamResultDTO(imgName);
        } catch (Exception e) {
            return new BaseResult<>("图片生成异常", false);
        }

        return new BaseResult<>(result, true);
    }

    //    测试实时摄像头 获取图片
    @GetMapping("/get/{imgName}")
    public void getImage(@PathVariable("imgName") String imgName, HttpServletResponse response) {
        response.setContentType("image/jpeg");
        String filePath = imageUtils.getGalleryPath() + imgName + ".jpeg";

        Path path = Paths.get(filePath);
        try {
            byte[] bytes = Files.readAllBytes(path);
            response.getOutputStream().write(bytes);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();//获取图片异常
        }
    }
    //endregion
}
