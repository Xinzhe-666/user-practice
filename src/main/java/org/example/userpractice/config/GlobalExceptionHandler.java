package org.example.userpractice.config;

import org.example.userpractice.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@RestControllerAdvice // 全局异常处理，统一返回JSON格式
public class GlobalExceptionHandler {

    // 1. 捕获@RequestBody实体类参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder errorMsg = new StringBuilder();
        for (FieldError error : fieldErrors) {
            errorMsg.append(error.getField()).append("：").append(error.getDefaultMessage()).append("；");
        }
        // 去掉最后一个多余的分号
        return Result.error(errorMsg.substring(0, errorMsg.length() - 1));
    }

    // 2. 捕获@RequestParam/@PathVariable简单参数校验异常
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMsg = violations.iterator().next().getMessage();
        return Result.error(errorMsg);
    }

    // 3. 捕获参数类型不匹配异常（比如ID传了字符串）
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return Result.error("参数格式错误，请传入正确的数字类型ID");
    }

    // 4. 捕获业务运行时异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

    // 5. 兜底异常处理：捕获所有未处理的系统异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        e.printStackTrace(); // 控制台打印异常，方便排查问题
        return Result.error("服务器内部错误，请稍后重试");
    }
}