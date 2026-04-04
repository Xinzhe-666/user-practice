package org.example.userpractice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.userpractice.common.JwtUtil;
import org.example.userpractice.common.Result;
import org.example.userpractice.entity.User;
import org.example.userpractice.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 处理登录、注册、查询、修改、删除等接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public Result register(@RequestBody @Valid User user) {
        // 1. 校验用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, user.getName());
        User existUser = userService.getOne(queryWrapper);
        if (existUser != null) {
            return Result.error("注册失败！用户名已存在");
        }

        // 2. 密码BCrypt加密
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        // 3. 保存用户到数据库
        boolean isSuccess = userService.save(user);
        if (isSuccess) {
            return Result.success("注册成功！新用户ID：" + user.getId());
        }
        return Result.error("注册失败！");
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result login(@RequestParam String name, @RequestParam String password) {
        // 1. 根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, name);
        User user = userService.getOne(queryWrapper);

        // 2. 用户不存在，返回错误
        if (user == null) {
            return Result.error(400, "登录失败！用户名或密码错误");
        }

        // 3. BCrypt密码校验（核心，不能直接用equals对比）
        if (!BCrypt.checkpw(password, user.getPassword())) {
            return Result.error(400, "登录失败！用户名或密码错误");
        }

        // 4. 生成JWT令牌
        String token = jwtUtil.generateToken(user.getId(), user.getName(), user.getRole());

        // 5. 封装返回结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("token", token);
        resultMap.put("user", user);

        return Result.success(resultMap);
    }

    /**
     * 根据ID查询用户信息
     */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result updateUser(@RequestBody User user) {
        boolean isSuccess = userService.updateUser(user);
        if (isSuccess) {
            return Result.success("更新成功！");
        }
        return Result.error("更新失败！");
    }

    /**
     * 根据ID删除用户
     */
    @DeleteMapping("/{id}")
    public Result deleteUserById(@PathVariable Integer id) {
        boolean isSuccess = userService.deleteUserById(id);
        if (isSuccess) {
            return Result.success("删除成功！");
        }
        return Result.error("删除失败！");
    }

    /**
     * 查询所有用户列表
     */
    @GetMapping("/list")
    public Result getUserList() {
        return Result.success(userService.list());
    }
}