package com.qiniu.web;

import com.qiniu.entity.vo.UserLoginVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页，登录页
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(@ModelAttribute("errorMsg") String errorMsg, @ModelAttribute("user") UserLoginVO user, Model model) {
        user.setAccount("admin");
        user.setPassword("111111");

        model.addAttribute("user", user);
        model.addAttribute("errorMsg", errorMsg);
        return "login";
    }
}
