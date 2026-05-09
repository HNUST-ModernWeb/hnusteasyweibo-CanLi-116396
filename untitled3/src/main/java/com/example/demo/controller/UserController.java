package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin  // 跨域
public class UserController {

    @Autowired
    private UserMapper userMapper;

    // 登录
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        User loginUser = userMapper.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            return Result.success(loginUser);
        } else {
            return Result.fail("账号或密码错误");
        }
    }

    // 注册
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        userMapper.register(user);
        return Result.success("注册成功");
    }

    // ✅【关键修复】修改资料 → 永久保存到数据库
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        System.out.println("===== 后端收到更新请求 =====");
        System.out.println("收到的用户数据：" + user);
        System.out.println("ID: " + user.getId());
        System.out.println("Name: " + user.getName());
        System.out.println("Intro: " + user.getIntro());
        System.out.println("Avatar: " + user.getAvatar());
        userMapper.updateById(user);
        return Result.success();
    }
}
