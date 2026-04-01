package org.example.userpractice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.userpractice.common.JwtUtil;
import org.example.userpractice.common.Result;
import org.example.userpractice.common.RoleConstants;
import org.example.userpractice.common.UserContext;
import org.example.userpractice.entity.User;
import org.example.userpractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 1. 查询所有用户
    @GetMapping("/list")
    public Result<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return Result.success(userList);
    }

    // 2. 根据ID查询用户
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    // 3. 新增用户
    @PostMapping("/add")
    public Result<String> addUser(@RequestBody @Valid User user) {
        boolean isSuccess = userService.addUser(user);
        if (isSuccess) {
            return Result.success("添加成功！新用户ID：" + user.getId());
        }
        return Result.error("添加失败！用户名已存在");
    }

    // 4. 修改用户信息接口（带权限校验）
    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody @Valid User user) {
        // 获取当前登录用户的角色
        String currentRole = UserContext.getCurrentUserRole();
        // 权限校验：只有管理员和超级管理员有权限
        if (!RoleConstants.ROLE_SUPER_ADMIN.equals(currentRole) && !RoleConstants.ROLE_ADMIN.equals(currentRole)) {
            return Result.error(403, "权限不足，仅管理员可执行该操作");
        }
        boolean isSuccess = userService.updateUser(user);
        if (isSuccess) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败，用户不存在");
    }

    // 5. 删除用户接口（带权限校验）
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Integer id) {
        // 获取当前登录用户的角色
        String currentRole = UserContext.getCurrentUserRole();
        // 权限校验：只有超级管理员有权限
        if (!RoleConstants.ROLE_SUPER_ADMIN.equals(currentRole)) {
            return Result.error(403, "权限不足，仅超级管理员可执行该操作");
        }
        boolean isSuccess = userService.deleteUserById(id);
        if (isSuccess) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败，用户不存在");
    }

    // 6. 多条件组合查询用户
    @GetMapping("/search")
    public Result<List<User>> searchUser(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String gender
    ) {
        List<User> userList = userService.getUserByCondition(name, minAge, maxAge, gender);
        return Result.success(userList);
    }

    // 7. 带条件的分页查询用户
    @GetMapping("/page")
    public Result<IPage<User>> getUserByPage(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String gender
    ) {
        IPage<User> pageResult = userService.getUserByPage(pageNum, pageSize, name, minAge, maxAge, gender);
        return Result.success(pageResult);
    }

    // 8. 用户注册接口
    @PostMapping("/register")
    public Result<String> register(@RequestBody @Valid User user) {
        boolean isSuccess = userService.register(user);
        if (isSuccess) {
            return Result.success("注册成功！新用户ID：" + user.getId());
        }
        return Result.error("注册失败！用户名已存在");
    }

    // 9. 用户登录接口（登录成功返回JWT令牌）
    @PostMapping("/login")
    public Result<String> login(
            @RequestParam @NotBlank(message = "用户名不能为空") String name,
            @RequestParam @NotBlank(message = "密码不能为空") String password
    ) {
        User loginUser = userService.login(name, password);
        if (loginUser == null) {
            return Result.error("登录失败！用户名或密码错误");
        }
        // 生成JWT令牌
        String token = jwtUtil.generateToken(loginUser.getId(), loginUser.getName(), loginUser.getRole());
        return Result.success(token);
    }
}