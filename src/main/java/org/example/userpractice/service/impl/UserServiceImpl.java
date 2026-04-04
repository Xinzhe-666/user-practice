package org.example.userpractice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.userpractice.entity.User;
import org.example.userpractice.mapper.UserMapper;
import org.example.userpractice.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUserById(Integer id) {
        return this.getById(id);
    }

    @Override
    public boolean updateUser(User user) {
        return this.updateById(user);
    }

    @Override
    public boolean deleteUserById(Integer id) {
        return this.removeById(id);
    }
}