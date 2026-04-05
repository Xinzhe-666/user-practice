package org.example.userpractice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.userpractice.common.UserContext;
import org.example.userpractice.entity.OperateLog;
import org.example.userpractice.service.OperateLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 操作日志AOP切面
 * 自动记录所有接口的操作日志
 */
@Aspect
@Component
public class OperateLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperateLogAspect.class);

    @Autowired
    private OperateLogService operateLogService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 切入点：拦截controller包下的所有接口
     */
    @Pointcut("execution(* org.example.userpractice.controller..*.*(..))")
    public void operateLogPointcut() {
    }

    /**
     * 环绕通知：记录操作日志
     */
    @Around("operateLogPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AOP切面开始执行，拦截到方法：{}", joinPoint.getSignature().getName());

        long startTime = System.currentTimeMillis();
        OperateLog operateLog = new OperateLog();
        Object result = null;

        try {
            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                log.warn("无法获取请求上下文，跳过部分日志信息");
            } else {
                HttpServletRequest request = attributes.getRequest();
                // ... 原来的代码
            }
            HttpServletRequest request = attributes.getRequest();

            // 设置操作人信息（未登录的用户，这两个字段为null，不影响日志记录）
            operateLog.setOperateUserId(UserContext.getCurrentUserId());
            operateLog.setOperateUserName(UserContext.getCurrentUsername());

            // 设置请求信息
            // 获取完整方法名（类名.方法名），更精准定位问题
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String fullMethodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
            operateLog.setRequestMethod(fullMethodName);

            operateLog.setRequestUri(request.getRequestURI());
            operateLog.setOperateType(getOperateType(request.getMethod()));

            // 设置请求参数（优化版：标准JSON格式 + 敏感信息脱敏）
            try {
                // 1. 把参数数组转换成标准的JSON字符串
                String params = OBJECT_MAPPER.writeValueAsString(Arrays.asList(joinPoint.getArgs()));

                // 2. 敏感信息脱敏：把password字段的值替换成***
                // 处理JSON格式：{"password":"123456"}
                params = params.replaceAll("\"password\":\"[^\"]*\"", "\"password\":\"***\"");
                // 处理表单格式：password=123456
                params = params.replaceAll("password=[^&]*", "password=***");

                operateLog.setRequestParams(params);
            } catch (Exception e) {
                log.error("请求参数序列化失败", e);
                operateLog.setRequestParams("参数序列化失败");
            }

            // 执行目标方法
            result = joinPoint.proceed();
            operateLog.setExecuteResult("成功");
            log.info("方法执行成功，准备保存日志");

        } catch (Exception e) {
            // 记录异常信息
            log.error("方法执行异常", e);
            operateLog.setExecuteResult("失败");
            operateLog.setErrorMessage(e.getMessage());
            throw e; // 继续抛出异常，不影响全局异常处理
        } finally {
            // 计算执行耗时
            long executeTime = System.currentTimeMillis() - startTime;
            operateLog.setExecuteTime(executeTime);
            operateLog.setOperateTime(LocalDateTime.now());

            // 保存日志
            try {
                operateLogService.saveOperateLog(operateLog);
                log.info("操作日志保存成功");
            } catch (Exception e) {
                log.error("操作日志保存失败", e);
            }
        }

        return result;
    }

    /**
     * 根据请求方法获取操作类型
     */
    private String getOperateType(String method) {
        return switch (method) {
            case "POST" -> "新增";
            case "PUT" -> "修改";
            case "DELETE" -> "删除";
            case "GET" -> "查询";
            default -> "其他";
        };
    }
}