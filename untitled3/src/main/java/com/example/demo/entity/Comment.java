package com.example.demo.entity;
import lombok.Data;

@Data // Lombok自动生成get/set方法
public class Comment {
    private Integer id;
    private Integer postId;
    private Integer userId;
    private String name;
    private String avatar;
    private String content;
    private String createTime;
}
