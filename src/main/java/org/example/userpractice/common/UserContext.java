package org.example.userpractice.common;

import org.example.userpractice.entity.User;

/**
 * 用户上下文工具类
 * 基于ThreadLocal存储当前登录用户信息
 */
public class UserContext {

    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();

    /**
     * 设置当前登录用户
     */
    public static void setCurrentUser(User user) {
        CURRENT_USER.set(user);
    }

    /**
     * 获取当前登录用户
     */
    public static User getCurrentUser() {
        return CURRENT_USER.get();
    }

    /**
     * 获取当前登录用户ID
     */
    public static Integer getCurrentUserId() {
        User user = CURRENT_USER.get();
        return user == null ? null : user.getId();
    }

    /**
     * 获取当前登录用户名（就是你缺失的这个方法）
     */
    public static String getCurrentUsername() {
        User user = CURRENT_USER.get();
        return user == null ? null : user.getName();
    }

    /**
     * 清除当前用户信息
     */
    public static void clear() {
        CURRENT_USER.remove();
    }
}