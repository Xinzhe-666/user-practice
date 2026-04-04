package org.example.userpractice.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.userpractice.entity.User;
import org.example.userpractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT登录拦截器：验证用户是否登录，拦截未登录的请求
 */
@Component // 交给Spring容器管理
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // Jackson工具类，用于返回JSON格式的错误信息
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行OPTIONS预检请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 2. 从请求头中获取Authorization令牌
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 没有令牌，返回401未授权
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
            response.getWriter().write(OBJECT_MAPPER.writeValueAsString(Result.error(401, "登录已过期，请重新登录")));
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

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束后，清除ThreadLocal中的用户信息，避免内存泄漏
        UserContext.clear();
    }
}