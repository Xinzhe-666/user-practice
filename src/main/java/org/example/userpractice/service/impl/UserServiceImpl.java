package org.example.userpractice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.userpractice.common.RedisUtil;
import org.example.userpractice.entity.User;
import org.example.userpractice.mapper.UserMapper;
import org.example.userpractice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private RedisUtil redisUtil;

    // 缓存key前缀：命名规范：业务名:数据类型:id
    private static final String USER_CACHE_KEY_PREFIX = "user:info:";
    // 缓存过期时间：1小时（3600秒）
    private static final long CACHE_EXPIRE_TIME = 3600;

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

    /**
     * 根据ID查询用户（带缓存）
     * 华为标准缓存流程：先查缓存 → 缓存没有查数据库 → 查到后存入缓存 → 返回结果
     */
    @Override
    public User getById(Serializable id) {
        String cacheKey = USER_CACHE_KEY_PREFIX + id;

        User user = (User) redisUtil.get(cacheKey);
        if (user != null) {
            log.info("从Redis缓存中获取用户：{}", id);
            return user;
        }

        log.info("Redis缓存未命中，查询数据库：{}", id);
        user = super.getById(id);

        if (user != null) {
            // 正常数据，缓存1小时
            // 【修复1】添加时间单位 TimeUnit.SECONDS
            redisUtil.set(cacheKey, user, CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            log.info("用户信息存入Redis缓存：{}", id);
        } else {
            // 数据库里没有这个用户，缓存空值，过期时间5分钟
            // 【修复1】添加时间单位 TimeUnit.SECONDS
            redisUtil.set(cacheKey, null, 300, TimeUnit.SECONDS);
            log.info("用户不存在，缓存空值：{}", id);
        }

        return user;
    }

    /**
     * 更新用户信息（同步删除缓存）
     */
    @Override
    public boolean updateById(User entity) {
        // 1. 先更新数据库
        boolean success = super.updateById(entity);

        // 2. 更新成功，删除Redis缓存
        if (success) {
            String cacheKey = USER_CACHE_KEY_PREFIX + entity.getId();
            redisUtil.delete(cacheKey);
            log.info("用户信息更新，删除Redis缓存：{}", entity.getId());
        }

        // 3. 返回结果
        return success;
    }

    /**
     * 删除用户（同步删除缓存）
     */
    @Override
    public boolean removeById(Serializable id) {
        // 1. 先删除数据库
        boolean success = super.removeById(id);

        // 2. 删除成功，删除Redis缓存
        if (success) {
            String cacheKey = USER_CACHE_KEY_PREFIX + id;
            redisUtil.delete(cacheKey);
            log.info("用户删除，删除Redis缓存：{}", id);
        }

        // 3. 返回结果
        return success;
    }
}