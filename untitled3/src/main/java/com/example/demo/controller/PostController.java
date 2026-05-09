package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/list")
    public Result<List<Post>> list() {
        List<Post> list = postService.list();

        for (Post post : list) {
            if (post.getUserId() != null) {
                User user = userMapper.getById(post.getUserId());
                if (user != null) {
                    post.setName(user.getName());
                    post.setAvatar(user.getAvatar());
                } else {
                    post.setName("未知用户");
                }
            } else {
                post.setName("未知用户");
            }
        }

        return Result.success(list);
    }
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.fail("文件不能为空");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + "/upload/";

        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest = new File(filePath + fileName);
        file.transferTo(dest);

        return Result.success("/upload/" + fileName);
    }


    @PostMapping("/add")
    public Result<String> add(@RequestBody Post post) {
        postService.add(post);
        return Result.success("发布成功");
    }
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Integer id) {
        postService.delete(id);
        return Result.success("删除成功");
    }
    @PostMapping("/like/{id}")
    public Result like(@PathVariable Integer id, @RequestParam Boolean isLike) {
        postService.changeLike(id, isLike);
        return Result.success();
    }
}