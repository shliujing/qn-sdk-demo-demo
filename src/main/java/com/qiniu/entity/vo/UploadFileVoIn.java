package com.qiniu.entity.vo;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileVoIn extends UpTokenVoIn {
    private String key;//文件名
    private MultipartFile file;//文件

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
