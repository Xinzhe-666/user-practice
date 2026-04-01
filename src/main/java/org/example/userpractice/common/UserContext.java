package org.example.userpractice.common;

import org.example.userpractice.entity.User;

/**
 * 当前登录用户上下文
 * 基于ThreadLocal实现，线程隔离，保证用户信息的线程安全
 */
public class UserContext {

    // ThreadLocal：每个线程独立存储，不会和其他线程混淆
    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();

    /**
     * 设置当前登录的用户信息
     */
    public static void setCurrentUser(User user) {
        CURRENT_USER.set(user);
    }

    /**
     * 获取当前登录的用户信息
     */
    public static User getCurrentUser() {
        return CURRENT_USER.get();
    }

    /**
     * 获取当前登录用户的ID
     */
    public static Integer getCurrentUserId() {
        User user = CURRENT_USER.get();
        return user == null ? null : user.getId();
    }

    /**
     * 获取当前登录用户的角色
     */
    public static String getCurrentUserRole() {
        User user = CURRENT_USER.get();
        return user == null ? null : user.getRole();
    }

    /**
     * 清除当前用户信息
     * 必须在请求结束后调用，防止ThreadLocal内存泄漏
     */
    public static void clear() {
        CURRENT_USER.remove();
    }
}