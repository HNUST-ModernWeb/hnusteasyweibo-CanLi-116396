package com.example.demo.mapper;

import com.example.demo.entity.Comment;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CommentMapper {

    // 新增评论
    @Insert("INSERT INTO comment(post_id, user_id, name, avatar, content) " +
            "VALUES(#{postId}, #{userId}, #{name}, #{avatar}, #{content})")
    int insert(Comment comment);

    // 根据帖子ID查询所有评论
    @Select("SELECT * FROM comment WHERE post_id = #{postId} ORDER BY create_time ASC")
    List<Comment> listByPostId(Integer postId);

    // 删除评论
    @Delete("DELETE FROM comment WHERE id = #{id}")
    int deleteById(Integer id);
}
