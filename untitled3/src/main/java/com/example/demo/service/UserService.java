package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public User login(String username, String password) {
        return userMapper.login(username, password);
    }

    public void register(User user) {
        userMapper.register(user);
    }

    public void update(User user) {
        userMapper.updateById(user);
    }
}