package com.yangle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yangle on 2017/9/24.
 */
@Controller
public class LoginController {
    @RequestMapping("/login")
public String login(Model model, HttpServletRequest request,String error, String logout) {
    if (logout != null) {
        model.addAttribute("msg", "你已经成功退出");
    }
    model.addAttribute("ctx",request.getContextPath());

    return "login";
}
}
