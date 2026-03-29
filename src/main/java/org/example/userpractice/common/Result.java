package org.example.userpractice.common;

import lombok.Data;

/**
 * 全局统一返回结果类
 */
@Data
public class Result<T> {
    // 响应状态码：200成功，400参数错误/业务异常，500系统异常
    private Integer code;
    // 响应提示信息
    private String msg;
    // 响应数据
    private T data;

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // 成功响应（仅提示）
    public static <T> Result<T> success(String msg) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    // 失败响应
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(400);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    // 失败响应（自定义状态码）
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}