package com.example.demo.entity;

import lombok.Data;

@Data // Lombok自动生成get/set方法
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String intro;
    private String avatar;
}
