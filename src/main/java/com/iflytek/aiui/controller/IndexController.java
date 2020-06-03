package com.iflytek.aiui.controller;


import com.iflytek.aiui.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    public static final Logger looger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String preLogin() {
        return "login";
    }

    @RequestMapping({"/", "/index"})
    public String index(Model model, HttpSession session) {
        try {
            looger.info("执行了index页面");
            model.addAttribute("user", session.getAttribute("user"));
        } catch (Exception e) {
            looger.error(e.getMessage());
        }
        return "main/index";
    }

    @RequestMapping("/loginFace")
    public String preLoginFace() {
        return "loginFace";
    }


    @GetMapping("/registerface")
    public String preFaceRegister() {
        return "registerFace";
    }


    @GetMapping("/register")
    public String preRegister() {
        return "register";
    }


    @GetMapping("/aiui/voice2text")
    public String preVoice2text() {
        return "main/voice2text";
    }

    @GetMapping("/aiui/voice2search")
    public  String preVoice2search(Model model, String condition){
        if(condition == null){
            looger.info("voice2search: user is null");
            model.addAttribute("userList", userService.list());
        }else{
            looger.info("voice2search: user is not null"+condition);
            model.addAttribute("userList", userService.voiceSearch(condition));
        }
        return "main/voice2search";
    }

    @GetMapping("/aiui/voice2translation")
    public String preVoice2translation() {
        return "main/voice2translation";
    }
}
