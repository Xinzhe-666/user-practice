package org.example.userpractice.common;

/**
 * 系统角色常量定义
 * 统一管理全系统的角色标识，避免硬编码
 */
public class RoleConstants {
    /**
     * 超级管理员：拥有系统最高权限
     */
    public static final String ROLE_SUPER_ADMIN = "super_admin";
    /**
     * 普通管理员：拥有用户管理权限
     */
    public static final String ROLE_ADMIN = "admin";
    /**
     * 普通用户：基础访问权限
     */
    public static final String ROLE_USER = "user";
    private RoleConstants() {
    }
}