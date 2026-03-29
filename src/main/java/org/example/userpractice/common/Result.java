package org.example.userpractice.common;

// 企业级通用接口返回格式
public class Result {
    // 状态码：200=成功，500=失败
    private Integer code;
    // 提示信息
    private String msg;
    // 要返回的数据
    private Object data;

    // 成功的静态方法（带数据）
    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    // 失败的静态方法（带错误信息）
    public static Result error(String msg) {
        Result result = new Result();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    // getter和setter
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
