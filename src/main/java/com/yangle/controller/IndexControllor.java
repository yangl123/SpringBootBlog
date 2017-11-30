package com.yangle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yangle on 2017/11/30.
 */
@Controller
public class IndexControllor {
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

}
