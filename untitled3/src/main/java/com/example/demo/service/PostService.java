package com.example.demo.service;

import com.example.demo.entity.Post;
import com.example.demo.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostMapper postMapper;

    public List<Post> list(){
        return postMapper.list();
    }


    public int add(Post post){
        return postMapper.add(post);
    }
    public void delete(Integer id) {
        postMapper.delete(id);
    }
    public void changeLike(Integer postId, Boolean isLike) {
        Post post = postMapper.getById(postId);
        if (isLike) {
            post.setLike(post.getLike() + 1);
        } else {
            post.setLike(post.getLike() - 1);
        }
        postMapper.updateById(post);
    }
}