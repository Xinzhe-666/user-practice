package org.example.userpractice.common;

import org.example.userpractice.entity.User;

public class UserContext {

    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();

    public static void setCurrentUser(User user) {
        CURRENT_USER.set(user);
    }

    public static User getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static Integer getCurrentUserId() {
        User user = CURRENT_USER.get();
        return user == null ? null : user.getId();
    }

    public static String getCurrentUsername() {
        User user = CURRENT_USER.get();
        return user == null ? null : user.getName();
    }

    public static void clear() {
        CURRENT_USER.remove();
    }
}