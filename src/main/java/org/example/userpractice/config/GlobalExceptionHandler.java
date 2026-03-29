package org.example.userpractice.config;

import org.example.userpractice.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
// 全版本兼容的异常类导入
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 兜底异常处理：捕获所有Exception，处理所有未单独匹配的异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleException(Exception e) {
        // 控制台打印异常，方便排查问题
        e.printStackTrace();
        // 修正语法错误：去掉非法的msg: 直接传字符串
        return Result.error("服务器内部错误，请稍后重试");
    }

    // 2. 业务运行时异常：处理代码里主动抛出的业务异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

    // 3. 参数校验异常：处理JSR-303注解校验不通过的异常
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleBindException(BindException e) {
        String errorMsg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.error("参数校验失败：" + errorMsg);
    }

    // 4. 参数类型不匹配异常：字符串转数字失败等场景
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        e.printStackTrace();
        return Result.error("参数格式错误，请传入正确的数字类型ID");
    }

    // 5. 404接口不存在异常
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handleNoHandlerFoundException(NoHandlerFoundException e) {
        e.printStackTrace();
        return Result.error("请求的接口不存在，请检查地址是否正确");
    }
}