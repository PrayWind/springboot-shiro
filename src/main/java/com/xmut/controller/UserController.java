package com.xmut.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description:
 * @author: whf
 * @date:
 */
@Controller
public class UserController {

    /*
     * @Description: 测试环境搭建方法
     * @Author: whf
     * @Date: 2019/8/14
     * @Param:
     * @Return:
     */
    @ResponseBody
    @RequestMapping("/hello")
    public String Hello() {

        System.out.println("hello");
        return "ok";
    }

    /*
     * @Description: 测试Thymeleaf
     * @Author: whf
     * @Date: 2019/8/14
     * @Param: [model]
     * @Return: java.lang.String
     */
    @RequestMapping("/testThymeleaf")
    public String testThymeleaf(Model model) {

        model.addAttribute("name", "PrayWind");
        return "/test";
    }

    /*
     * @Description: 添加
     * @Author: whf
     * @Date: 2019/8/14
     * @Param: []
     * @Return: java.lang.String
     */
    @RequestMapping("/add")
    public String add() {
        return "/user/add";
    }

    /*
     * @Description: 修改
     * @Author: whf
     * @Date: 2019/8/14
     * @Param: []
     * @Return: java.lang.String
     */
    @RequestMapping("/update")
    public String update() {
        return "/user/update";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "/login";
    }

    /*
     * @Description: 登录处理
     * @Author: whf
     * @Date: 2019/8/14
     * @Param: [username, password]
     * @Return: java.lang.String
     */
    @RequestMapping("/login")
    public String login(String username, String password, Model model) {
        // 使用shiro处理登录
        // 1. 获取Subject
        Subject subject = SecurityUtils.getSubject();
        // 2. 封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 3. 执行登录方法
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            // 用户名不存在
            model.addAttribute("msg", "用户名不存在");
            return "/login";
        } catch (IncorrectCredentialsException e) {
            // 密码错误
            model.addAttribute("msg", "密码错误");
            return "/login";
        }

        // 冒号后面不能加空格！！！不然请求路径会多出 %20
        return "redirect:/testThymeleaf";
    }

    @RequestMapping("/noAuth")
    public String noAuth() {
        return "/noAuth";
    }
}
