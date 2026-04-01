package org.example.userpractice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.userpractice.common.JwtUtil;
import org.example.userpractice.common.Result;
import org.example.userpractice.common.UserContext;
import org.example.userpractice.entity.User;
import org.example.userpractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT令牌统一拦截器
 * 在请求进入Controller之前，统一验证令牌的合法性
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // Jackson工具：把Java对象转换成JSON字符串，返回给前端
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 请求进入Controller之前执行：统一验证令牌
     * @return true=放行请求，false=拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头中获取令牌，前端标准格式：Authorization: Bearer 令牌内容
        String authHeader = request.getHeader("Authorization");

        // 2. 判断请求头中是否有合法的令牌前缀
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 没有令牌，返回401未授权，提示请先登录
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(Result.error(401, "请先登录")));
            return false;
        }

        // 3. 提取令牌内容：去掉前面的"Bearer "前缀（7个字符）
        String token = authHeader.substring(7);

        // 4. 验证令牌是否合法、有没有过期
        if (!jwtUtil.validateToken(token)) {
            // 令牌无效/过期，返回401未授权
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(Result.error(401, "令牌无效或已过期，请重新登录")));
            return false;
        }

        // 5. 令牌有效，解析用户ID，查询用户完整信息
        Integer userId = jwtUtil.getUserIdFromToken(token);
        User currentUser = userService.getUserById(userId);
        if (currentUser == null) {
            // 用户不存在，返回401未授权
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(Result.error(401, "用户不存在，请重新登录")));
            return false;
        }

        // 6. 把当前登录的用户信息，放到UserContext中，后续Controller/Service可以直接获取
        UserContext.setCurrentUser(currentUser);

        // 7. 所有验证通过，放行请求，进入Controller
        return true;
    }

    /**
     * 请求处理完成后执行：清除ThreadLocal中的用户信息，防止内存泄漏
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.clear();
    }
}