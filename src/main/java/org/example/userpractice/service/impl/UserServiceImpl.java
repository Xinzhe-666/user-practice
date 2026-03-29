package org.example.userpractice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.userpractice.entity.User;
import org.example.userpractice.mapper.UserMapper;
import org.example.userpractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// @Service注解：告诉Spring Boot，这是业务层实现类，把它交给Spring管理，这样Controller才能注入它
@Service
public class UserServiceImpl implements UserService {

    // 注入Mapper：Service层不直接操作数据库，只调用Mapper
    @Autowired
    private UserMapper userMapper;

    // 查询所有用户：业务逻辑简单，直接调用Mapper查所有
    @Override
    public List<User> getAllUsers() {
        // selectList(null)：MyBatis-Plus的方法，null表示没有查询条件，查所有
        return userMapper.selectList(null);
    }

    // 根据ID查询用户：直接调用Mapper按主键查
    @Override
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    // 添加用户：修正后的正确代码
    @Override
    public boolean addUser(User user) {
        // 业务规则1：判断姓名是否已存在（不能有重名用户）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 修正1：数据库字段名改成name，方法改成getName()，和实体类、表完全对应
        queryWrapper.eq("name", user.getName());
        // 查询是否有重名用户
        User existUser = userMapper.selectOne(queryWrapper);
        if (existUser != null) {
            return false; // 姓名已存在，添加失败
        }

        // 业务规则2：年龄不能小于0（非法年龄）
        if (user.getAge() < 0) {
            return false;
        }

        // 业务规则3：密码长度不能少于6位
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return false;
        }

        // 所有规则通过，插入数据到数据库
        int rows = userMapper.insert(user);
        return rows > 0; // 插入成功返回true，失败返回false
    }

    // 修改用户
    @Override
    public boolean updateUser(User user) {
        // 先判断要修改的用户是否存在
        User existUser = userMapper.selectById(user.getId());
        if (existUser == null) {
            // 用户不存在，返回修改失败
            return false;
        }
        // 用户存在，调用Mapper根据ID更新
        int rows = userMapper.updateById(user);
        return rows > 0;
    }

    // 根据ID删除用户
    @Override
    public boolean deleteUserById(Integer id) {
        // 先判断要删除的用户是否存在
        User existUser = userMapper.selectById(id);
        if (existUser == null) {
            // 用户不存在，返回删除失败
            return false;
        }
        // 用户存在，调用Mapper根据ID删除
        int rows = userMapper.deleteById(id);
        return rows > 0;
    }

    @Override
    public List<User> getUserByCondition(String name, Integer minAge, Integer maxAge, String gender) {
        // 1. 创建Lambda条件构造器，泛型是User实体类
        LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper<>();

        // 2. 拼接条件：不用写字符串字段名，直接用User::getName引用属性，编译期检查
        // 姓名模糊搜索
        lambdaQuery.like(StringUtils.hasLength(name), User::getName, name);
        // 性别精准匹配
        lambdaQuery.eq(StringUtils.hasLength(gender), User::getGender, gender);
        // 最小年龄
        lambdaQuery.ge(minAge != null, User::getAge, minAge);
        // 最大年龄
        lambdaQuery.le(maxAge != null, User::getAge, maxAge);

        // 3. 排序
        lambdaQuery.orderByDesc(User::getAge).orderByAsc(User::getId);

        // 4. 执行查询
        return userMapper.selectList(lambdaQuery);
    }

    @Override
    public IPage<User> getUserByPage(Long pageNum, Long pageSize, String name, Integer minAge, Integer maxAge, String gender) {
        // 1. 创建分页对象：参数1=当前页码，参数2=每页条数
        // 注意：MyBatis-Plus的页码是从1开始的，不用自己算偏移量，它会自动处理
        Page<User> page = new Page<>(pageNum, pageSize);

        // 2. 创建Lambda条件构造器，拼接搜索条件（和之前学的条件查询完全一样）
        LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper<>();
        // 姓名模糊搜索
        lambdaQuery.like(StringUtils.hasText(name), User::getName, name);
        // 性别精准匹配
        lambdaQuery.eq(StringUtils.hasText(gender), User::getGender, gender);
        // 最小年龄
        lambdaQuery.ge(minAge != null, User::getAge, minAge);
        // 最大年龄
        lambdaQuery.le(maxAge != null, User::getAge, maxAge);
        // 排序：按年龄降序
        lambdaQuery.orderByDesc(User::getAge);

        // 3. 执行分页查询：Mapper自带的selectPage方法，传入分页对象和条件构造器
        // 这一步会自动执行2条SQL：① 带条件的分页查询SQL ② 带条件的COUNT(*)统计总条数SQL
        IPage<User> userPage = userMapper.selectPage(page, lambdaQuery);

        // 4. 返回分页结果
        return userPage;
    }
}