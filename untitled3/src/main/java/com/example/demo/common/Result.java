package com.example.demo.common;

import lombok.Data;

@Data
public class Result<T> {
    private int code; // 状态码：200成功，500失败
    private String msg; // 提示信息
    private T data; // 返回的数据
    public static <T> Result<T> success() {
        Result<T> r = new Result<>();
        r.code = 200;
        r.msg = "成功";
        r.data = null;
        return r;
    }

    // 成功响应
    public static <T> Result<T> success(T data){
        Result<T> r = new Result<>();
        r.code = 200;
        r.msg = "成功";
        r.data = data;
        return r;
    }

    // 失败响应
    public static <T> Result<T> fail(String msg){
        Result<T> r = new Result<>();
        r.code = 500;
        r.msg = msg;
        return r;
    }
}