package com.hwx.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {

    @RequestMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @RequestMapping("/user/login")
    public String login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        Subject subject = SecurityUtils.getSubject();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        try {
            subject.login(usernamePasswordToken);
        }catch (Exception e){
            e.printStackTrace();
            return "/login";
        }
        return "/index";
    }

}
