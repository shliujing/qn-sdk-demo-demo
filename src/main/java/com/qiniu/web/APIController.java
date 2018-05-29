package com.qiniu.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.Const;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.common.enums.UserTypeEnum;
import com.qiniu.entity.BaseResult;
import com.qiniu.entity.User;
import com.qiniu.entity.vo.AccessTokenVoIn;
import com.qiniu.entity.vo.AccessTokenVoOut;
import com.qiniu.entity.vo.DownTokenVoIn;
import com.qiniu.entity.vo.UpTokenVoIn;
import com.qiniu.http.Response;
import com.qiniu.service.UserService;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 数据处理
 */
@RequestMapping("api")
@Controller
public class APIController {
    @Resource
    UserService userService;

    String filePath = "/Users/jingliu/Desktop/";

    @RequestMapping("/index")
    public String index(HttpServletRequest request, @ModelAttribute("errorMsg") String errorMsg, Model model) {
        //todo
        return "redirect:/api/token";
    }

    @RequestMapping("/token")
    public String token(HttpServletRequest request, @ModelAttribute("errorMsg") String errorMsg, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !UserTypeEnum.isAdmin(user.getUserTypeName())) {
            return "redirect:/";
        }

        List<User> users = userService.getUserList();
        model.addAttribute("users", users);
        model.addAttribute("errorMsg", errorMsg);
        return "api/token";
    }

    @RequestMapping("/upload")
    public String upload(HttpServletRequest request, @ModelAttribute("errorMsg") String errorMsg, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !UserTypeEnum.isAdmin(user.getUserTypeName())) {
            return "redirect:/";
        }

        List<User> users = userService.getUserList();
        model.addAttribute("users", users);
        model.addAttribute("errorMsg", errorMsg);
        return "api/upload";
    }

    @RequestMapping("/genAccessToken")
    @ResponseBody
    public BaseResult<AccessTokenVoOut> genToken(AccessTokenVoIn tokenVoIn) {
        AccessTokenVoOut result = new AccessTokenVoOut();
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = tokenVoIn.getAk();
        String SECRET_KEY = tokenVoIn.getSk();
        String url = tokenVoIn.getUrl();
        try {
            //密钥配置
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String token = auth.sign(url);
            result.setToken(token);
            result.setJson(JSON.toJSONString(token));
        } catch (Exception e) {
            return new BaseResult<>(e.getMessage(), false);
        }
        return new BaseResult<>(result, true);
    }

    @RequestMapping("/genUpToken")
    @ResponseBody
    public BaseResult<AccessTokenVoOut> genUpToken(UpTokenVoIn tokenVoIn) {
        AccessTokenVoOut result = new AccessTokenVoOut();
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = tokenVoIn.getAk();
        String SECRET_KEY = tokenVoIn.getSk();
        String bucket = tokenVoIn.getBucket();
        String key = tokenVoIn.getKey();
        try {
            //密钥配置
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String token;
            if (key == null || key.equals("")) {
                token = auth.uploadToken(bucket);
            } else {
                token = auth.uploadToken(bucket, key);
            }
            result.setToken(token);
            result.setJson(JSON.toJSONString(token));
        } catch (Exception e) {
            return new BaseResult<>(e.getMessage(), false);
        }
        return new BaseResult<>(result, true);
    }

    @RequestMapping("/genDownToken")
    @ResponseBody
    public BaseResult<AccessTokenVoOut> genDownToken(DownTokenVoIn tokenVoIn) {
        AccessTokenVoOut result = new AccessTokenVoOut();
        //设置好账号的ACCESS_KEY和SECRET_KEY
        String ACCESS_KEY = tokenVoIn.getAk();
        String SECRET_KEY = tokenVoIn.getSk();
        String url = tokenVoIn.getUrl();
        long expires = tokenVoIn.getExpires();
        try {
            //密钥配置
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String token;
            if (expires <= 0) {
                token = auth.privateDownloadUrl(url);
            } else {
                token = auth.privateDownloadUrl(url, expires);
            }

            result.setToken(token);
            result.setJson(JSON.toJSONString(token));
        } catch (Exception e) {
            return new BaseResult<>(e.getMessage(), false);
        }
        return new BaseResult<>(result, true);
    }


    //文件上传相关代码
    @RequestMapping("/uploadFile")
    @ResponseBody
    public String upload(@RequestParam("test") MultipartFile file) {
        if (file.isEmpty()) {
            return "文件为空";
        }
        String ACCESS_KEY = Const.ACCESS_KEY;
        String SECRET_KEY = Const.SECRET_KEY;
        String bucketname = "test-pub";
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        UploadManager uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
        String prefix = "test/sdk/file/";
        String fileName = file.getOriginalFilename();

        try {
            Response res = uploadManager.put(file.getBytes(), prefix + fileName, auth.uploadToken(bucketname));
            System.out.println(res.bodyString());
            return "上传成功";
        } catch (QiniuException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
//        return saveToLocal(file);
    }

    //多文件上传
    @RequestMapping(value = "/batchUploadFile", method = RequestMethod.POST)
    @ResponseBody
    public String batchUploadFile(MultipartHttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                .getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(
                            new File(file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                    stream = null;
                    return "You failed to upload " + i + " => "
                            + e.getMessage();
                }
            } else {
                return "You failed to upload " + i
                        + " because the file was empty.";
            }
        }
        return "upload successful";
    }


    //文件下载相关代码
    @RequestMapping("/download")
    public String downloadFile(org.apache.catalina.servlet4preview.http.HttpServletRequest request, HttpServletResponse response) {
        String fileName = "FileUploadTests.java";
        if (fileName != null) {
            //当前是从该工程的WEB-INF//File//下获取文件(该目录可以在下面一行代码配置)然后下载到C:\\users\\downloads即本机的默认下载的目录
            File file = new File(filePath, fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }


    private String saveToLocal(MultipartFile file) {
        if (file.isEmpty()) {
            return "文件为空";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 文件上传后的路径

        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return "上传成功";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

}
