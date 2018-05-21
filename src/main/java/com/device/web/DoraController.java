package com.device.web;

import com.device.common.enums.UserTypeEnum;
import com.device.entity.BaseResult;
import com.device.entity.User;
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

@RequestMapping("dora")
@Controller
public class DoraController {

}
