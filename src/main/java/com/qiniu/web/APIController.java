package com.qiniu.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.enums.UserTypeEnum;
import com.qiniu.entity.BaseResult;
import com.qiniu.entity.User;
import com.qiniu.entity.vo.AccessTokenVoIn;
import com.qiniu.entity.vo.AccessTokenVoOut;
import com.qiniu.service.UserService;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据处理
 */
@RequestMapping("api")
@Controller
public class APIController {
    @Resource
    UserService userService;

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

    @RequestMapping("/genToken")
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
}
