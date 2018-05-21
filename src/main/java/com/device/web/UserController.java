package com.device.web;

import com.device.common.enums.UserTypeEnum;
import com.device.entity.User;
import com.device.entity.BaseResult;
import com.device.entity.vo.UserLoginVO;
import com.device.service.UserService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 人员管理
 *   todo 登录验证放base
 */
@RequestMapping("user")
@Controller
public class UserController {
    @Resource
    UserService userService;

    @RequestMapping("/")
    public String index(@ModelAttribute("errorMsg") String errorMsg, @ModelAttribute("user") UserLoginVO user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("errorMsg", errorMsg);
        return "login";
    }

    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request, @Valid UserLoginVO user, BindingResult result,
                              RedirectAttributes redirect) {
        // 如果 user 的字段不符合要求,则返回到登录页面,并将 valid error 信息传入登录页面
        if (result.hasErrors()) {
            return new ModelAndView("login", "errorMsg", user.getErrors(result));
        }

        // 用户名或密码不正确
        User userDo = userService.findByAccountAndPassword(user);
        if (userDo==null) {
            // 添加错误消息,该消息将一起带到重定向后的页面,
            // 浏览器刷新后,该数据将消失
            redirect.addFlashAttribute("errorMsg", "登录失败,用户名或密码错误");
            // 重定向到 login.html 页面
            return new ModelAndView("redirect:/");
        }

        // 将用户登录信息添加到 session 中
        request.getSession().setAttribute("userAccount", userDo.getAccount());
        request.getSession().setAttribute("user", userDo);

        if (UserTypeEnum.ADMIN.getVal().equals(userDo.getUserTypeName())) {
            return new ModelAndView("redirect:/main/admin");
        } else {
            return new ModelAndView("redirect:/main/operator");
        }

    }

    @RequestMapping("/loginout")
    public ModelAndView loginout(HttpServletRequest request, User user) {
        request.getSession().setAttribute("userAccount", null);
        request.getSession().setAttribute("user", null);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/list")
    public String list(HttpServletRequest request, @ModelAttribute("errorMsg") String errorMsg, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !UserTypeEnum.isAdmin(user.getUserTypeName()) ) {
            return "redirect:/";
        }

        List<User> users = userService.getUserList();
        model.addAttribute("users", users);
        model.addAttribute("errorMsg", errorMsg);
        return "user/list";
    }

    @RequestMapping("/add")
    @ResponseBody
    public BaseResult<User> add(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return new BaseResult<>(user.getErrors(result).toString(), false);
        }

        try {
            userService.save(user);
        } catch (Exception e) {
            return new BaseResult<>(user.getAccount() + "已存在", false);
        }
        return new BaseResult<>(true);
    }

    @RequestMapping("/toEdit")
    @ResponseBody
    public BaseResult<User> toEdit(Long id) {
        User user = userService.findUserById(id);
        return new BaseResult<>(user, true);
    }

    @RequestMapping("/edit")
    @ResponseBody
    public BaseResult<User> edit(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return new BaseResult<>(user.getErrors(result).toString(), false);
        }

        try {
            userService.edit(user);
        } catch (Exception e) {
            return new BaseResult<>(user.getAccount() + "已存在", false);
        }
        return new BaseResult<>(true);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public BaseResult<User> delete(HttpServletRequest request, String account) {
        if (account == null || account.equals("admin") || account.equals("a")) {
            return new BaseResult<>("不能删除初始账号：" + account, false);
        }
        if (account != null && account.equalsIgnoreCase(String.valueOf(request.getSession().getAttribute("userAccount")))) {
            return new BaseResult<>("不能删除本账户：" + account, false);
        }

        userService.delete(account);
        return new BaseResult<>(true);
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "user/userAdd";
    }
}
