package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {

    @Select("select * from user where username=#{username} and password=#{password}")
    User login(String username, String password);

    @Insert("insert into user(username,password,name,intro,avatar) values(#{username},#{password},#{name},#{intro},#{avatar})")
    void register(User user);

    // ✅ 这个是真正保存到数据库
    @Update("<script>"
            + "UPDATE user"
            + "<set>"
            + "<if test='name != null'>name=#{name},</if>"
            + "<if test='intro != null'>intro=#{intro},</if>"
            + "<if test='avatar != null'>avatar=#{avatar},</if>"
            + "</set>"
            + "WHERE id=#{id}"
            + "</script>")
    void updateById(User user);
    @Select("select * from user where id = #{id}")
    User getById(Integer id);
}