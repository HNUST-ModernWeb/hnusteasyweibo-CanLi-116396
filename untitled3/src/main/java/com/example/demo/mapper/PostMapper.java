package com.example.demo.mapper;

import com.example.demo.entity.Post;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PostMapper {
    // 查询所有帖子 ✅ 已修复
    @Select("select id, title, content, `like`, user_id as userId, img_urls as imgUrls from post order by id desc")
    List<Post> list();

    // 🔥 新增帖子 + 存入 imgUrls
    @Insert("insert into post(title,content,user_id,img_urls) values(#{title},#{content},#{userId},#{imgUrls})")
    int add(Post post);
    @Delete("delete from post where id = #{id}")
    void delete(Integer id);
    @Select("select * from post where id = #{id}")
    Post getById(Integer id);

    @Update("update post set `like` = #{like} where id = #{id}")
    void updateById(Post post);
    }