package com.example.demo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Post {
    private Integer id;
    private String title;
    private String content;
    private Integer like;
    private Integer userId;
    private Date createTime;
    private String name;    // 作者昵称
    private String avatar;
    private String imgUrls;

}