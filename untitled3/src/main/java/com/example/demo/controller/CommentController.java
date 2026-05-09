package com.example.demo.controller;


import com.example.demo.entity.Comment;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.common.Result; // 你的通用返回类
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    // 新增评论
    @PostMapping("/add")
    public Result add(@RequestBody Comment comment) {
        commentMapper.insert(comment);
        return Result.success();
    }

    // 获取某帖子的所有评论
    @GetMapping("/listByPostId")
    public Result listByPostId(@RequestParam Integer postId) {
        List<Comment> list = commentMapper.listByPostId(postId);
        return Result.success(list);
    }

    // 删除评论
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        commentMapper.deleteById(id);
        return Result.success();
    }
}
