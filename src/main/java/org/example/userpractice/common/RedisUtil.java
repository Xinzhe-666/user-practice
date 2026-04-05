package org.example.userpractice.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类：封装常用的Redis操作
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ====================== 核心常用方法 ======================

    /**
     * 设置缓存，带过期时间
     * @param key 缓存的键
     * @param value 缓存的值
     * @param time 过期时间
     * @param timeUnit 时间单位
     * @return true=成功，false=失败
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, time, timeUnit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置缓存，不带过期时间
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取缓存
     * @param key 缓存的键
     * @return 缓存的值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存（支持单个/多个key）
     * @param key 缓存的键，可传多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                // 单个key，直接删除
                redisTemplate.delete(key[0]);
            } else {
                // 多个key，转成List<String>后批量删除，解决类型不匹配问题
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 判断缓存中是否存在key
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 给缓存设置过期时间
     */
    public boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 尝试获取分布式锁（解决缓存击穿用）
     * @param key 锁的key
     * @param value 锁的值
     * @param time 锁的过期时间
     * @param timeUnit 时间单位
     * @return true=获取锁成功，false=失败
     */
    public boolean setIfAbsent(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void delete(String cacheKey) {
    }
}