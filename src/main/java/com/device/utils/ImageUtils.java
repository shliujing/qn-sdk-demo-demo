package com.device.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUtils {

    @Value("${win.gallery.dir}")
    private  String winGallery;

    @Value("${linux.gallery.dir}")
    private  String linuxGallery;

    private  Path path = null;

    /**
     * 功能描述：base64字符串转换成图片
     *
     * @since 2016/5/24
     */
    public boolean generateImage(String imgStr, String filePath, String fileName) {
        try {
            if (imgStr == null) {
                return false;
            }
            BASE64Decoder decoder = new BASE64Decoder();
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            //如果目录不存在，则创建
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            //生成图片
            OutputStream out = new FileOutputStream(filePath + fileName);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    适用于传输文件流
     */
    public Boolean store(MultipartFile file, String fileName) {
        Boolean flag = false;
        if (file.isEmpty()) {
            throw new RuntimeException("Fail to store empty file");
        }

        try {
            path = Paths.get(getGalleryPath(), fileName);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag =  false;
        }

        return flag;
    }

    public  String getGalleryPath() {
        String osname = System.getProperty("os.name");
        String galleryPath = null;
        if (osname.startsWith("Windows")) {
            // 在 Windows 操作系统上
            galleryPath = winGallery;
        } else if (osname.startsWith("Linux")) {
            // 在 Linux 操作系统上
            galleryPath = linuxGallery;
        }
        return galleryPath;
    }
}
