package org.example.userpractice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.userpractice.entity.User;

// 告诉Spring这是一个数据库操作的Mapper接口
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 里面什么都不用写，BaseMapper自带所有增删改查方法
}