package org.example.userpractice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.userpractice.entity.User;

public interface UserService extends IService<User> {
    User getUserById(Integer id);
    boolean updateUser(User user);
    boolean deleteUserById(Integer id);
}