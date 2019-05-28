package com.hwx.controller;

import jdk.nashorn.internal.runtime.options.LoggingOption;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @RequestMapping("/user/login")
    public String login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        try {
            subject.login(usernamePasswordToken);
        }catch (UnknownAccountException e){
            logger.info("未知用户{}尝试登陆", username);
            return "/login";
        }catch (IncorrectCredentialsException e){
            logger.info("密码不正确");
            return "/login";
        }catch (ExcessiveAttemptsException e){
            logger.info("{}账号已被锁定", username);
            return "/login";
        }catch (Exception e){
            logger.info("认证失败");
            return "/login";
        }
        return "/index";
    }

}
