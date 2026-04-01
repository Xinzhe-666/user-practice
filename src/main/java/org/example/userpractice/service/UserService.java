package org.example.userpractice.service;

import org.example.userpractice.entity.User;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;

// 用户业务层接口：就像「岗位职责说明书」，规定了业务经理能做哪些事
public interface UserService {
    // 1. 查询所有用户：方法名见名知意，返回值是用户列表
    List<User> getAllUsers();

    // 2. 根据ID查询用户：参数是用户ID，返回值是单个用户
    User getUserById(Integer id);

    // 3. 添加用户：参数是要添加的用户对象，返回值是boolean（true=成功，false=失败）
    boolean addUser(User user);

    // 4. 修改用户：参数是要修改的用户对象（必须带ID），返回值是boolean
    boolean updateUser(User user);

    // 5. 根据ID删除用户：参数是要删除的用户ID，返回值是boolean
    boolean deleteUserById(Integer id);

    // 多条件组合查询用户
    List<User> getUserByCondition(String name, Integer minAge, Integer maxAge, String gender);

    // 带条件的分页查询用户
    // 参数：pageNum=当前页码，pageSize=每页条数，其他是搜索条件
    IPage<User> getUserByPage(Long pageNum, Long pageSize, String name, Integer minAge, Integer maxAge, String gender);

    // 用户注册：返回true=注册成功，false=注册失败（用户名已存在）
    boolean register(User user);
    // 用户登录：返回null=登录失败（用户名不存在/密码错误），返回用户对象=登录成功
    User login(String name, String password);
}