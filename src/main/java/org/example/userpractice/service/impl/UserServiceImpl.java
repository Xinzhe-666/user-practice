package org.example.userpractice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.userpractice.common.RedisUtil;
import org.example.userpractice.entity.User;
import org.example.userpractice.mapper.UserMapper;
import org.example.userpractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // 注入加密工具
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtil redisUtil;

    // 你之前已有的方法，不用改
    @Override
    public List<User> getAllUsers() {
        return this.list();
    }

    @Override
    public User getUserById(Integer id) {
        String cacheKey = "user:info:" + id;
        String lockKey = "lock:user:" + id; // 锁的key，每个用户ID对应一个锁

        // 1. 先查缓存
        User cacheUser = (User) redisUtil.get(cacheKey);
        if (cacheUser != null) {
            return cacheUser;
        }

        // 2. 缓存未命中，尝试获取互斥锁
        try {
            // 尝试获取锁，设置锁的过期时间10秒，防止死锁
            boolean getLock = redisUtil.setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
            if (getLock) {
                // 拿到锁了，去查数据库
                User user = this.getById(id);
                if (user != null) {
                    redisUtil.set(cacheKey, user, 30, TimeUnit.MINUTES);
                } else {
                    redisUtil.set(cacheKey, null, 2, TimeUnit.MINUTES);
                }
                return user;
            } else {
                // 没拿到锁，等待100毫秒，重新查缓存
                Thread.sleep(100);
                return getUserById(id); // 递归重试
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 最后一定要释放锁
            redisUtil.del(lockKey);
        }

        return null;
    }

    @Override
    public boolean addUser(User user) {
        // 先判断用户名是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, user.getName());
        User existUser = this.getOne(queryWrapper);
        if (existUser != null) {
            return false;
        }
        return this.save(user);
    }

    @Override
    public boolean updateUser(User user) {
        boolean isSuccess = this.updateById(user);
        if (isSuccess) {
            // 更新数据库成功后，删除对应的缓存，下次查询会重新加载最新的数据
            String cacheKey = "user:info:" + user.getId();
            redisUtil.del(cacheKey);
        }
        return isSuccess;
    }

    @Override
    public boolean deleteUserById(Integer id) {
        boolean isSuccess = this.removeById(id);
        if (isSuccess) {
            // 删除数据库数据后，删除对应的缓存
            String cacheKey = "user:info:" + id;
            redisUtil.del(cacheKey);
        }
        return isSuccess;
    }

    @Override
    public List<User> getUserByCondition(String name, Integer minAge, Integer maxAge, String gender) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(User::getName, name);
        }
        if (minAge != null) {
            queryWrapper.ge(User::getAge, minAge);
        }
        if (maxAge != null) {
            queryWrapper.le(User::getAge, maxAge);
        }
        if (gender != null && !gender.isEmpty()) {
            queryWrapper.eq(User::getGender, gender);
        }
        return this.list(queryWrapper);
    }

    @Override
    public IPage<User> getUserByPage(Long pageNum, Long pageSize, String name, Integer minAge, Integer maxAge, String gender) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(User::getName, name);
        }
        if (minAge != null) {
            queryWrapper.ge(User::getAge, minAge);
        }
        if (maxAge != null) {
            queryWrapper.le(User::getAge, maxAge);
        }
        if (gender != null && !gender.isEmpty()) {
            queryWrapper.eq(User::getGender, gender);
        }
        return this.page(page, queryWrapper);
    }

    // 今天新增的注册方法实现
    @Override
    public boolean register(User user) {
        // 1. 先判断用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, user.getName());
        User existUser = this.getOne(queryWrapper);
        if (existUser != null) {
            return false; // 用户名已存在，注册失败
        }

        // 2. 【核心】对密码进行BCrypt加密，绝对不能明文存到数据库
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // 3. 保存用户到数据库
        return this.save(user);
    }

    // 今天新增的登录方法实现
    @Override
    public User login(String name, String password) {
        // 1. 根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, name);
        User user = this.getOne(queryWrapper);

        // 2. 用户名不存在，直接返回null，登录失败
        if (user == null) {
            return null;
        }

        // 3. 【核心】校验密码是否正确：BCrypt用matches方法比对，不能自己解密
        boolean isPasswordCorrect = passwordEncoder.matches(password, user.getPassword());
        if (!isPasswordCorrect) {
            return null; // 密码错误，登录失败
        }

        // 4. 用户名和密码都正确，返回用户对象，登录成功
        return user;
    }
}