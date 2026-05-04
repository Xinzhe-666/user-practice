package org.example.userpractice.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.userpractice.common.JwtUtil;
import org.example.userpractice.common.Result;
import org.example.userpractice.common.RoleConstants;
import org.example.userpractice.common.UserContext;
import org.example.userpractice.entity.User;
import org.example.userpractice.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Result<String> register(@RequestBody @Valid User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, user.getName());
        User existUser = userService.getOne(queryWrapper);
        if (existUser != null) {
            return Result.error("注册失败！用户名已存在");
        }

        if (!StringUtils.hasText(user.getRole())) {
            user.setRole(RoleConstants.ROLE_USER);
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        boolean isSuccess = userService.save(user);
        return isSuccess ? Result.success("注册成功！新用户ID：" + user.getId()) : Result.error("注册失败！");
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestParam String name, @RequestParam String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, name);
        User user = userService.getOne(queryWrapper);

        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            return Result.error(400, "登录失败！用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getName(), user.getRole());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("token", token);
        resultMap.put("user", toSafeUser(user));
        return Result.success(resultMap);
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getUserById(@PathVariable Integer id) {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            return Result.error(401, "请先登录");
        }
        if (!Objects.equals(currentUser.getId(), id) && !isAdmin(currentUser)) {
            return Result.error(403, "无权查看其他用户信息");
        }

        User user = userService.getUserById(id);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        return Result.success(toSafeUser(user));
    }

    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody User user) {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            return Result.error(401, "请先登录");
        }
        if (user.getId() == null) {
            return Result.error(400, "用户ID不能为空");
        }
        if (!Objects.equals(currentUser.getId(), user.getId()) && !isAdmin(currentUser)) {
            return Result.error(403, "无权修改其他用户信息");
        }

        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        } else {
            user.setPassword(null);
        }

        if (!isAdmin(currentUser)) {
            user.setRole(null);
        }

        boolean isSuccess = userService.updateUser(user);
        return isSuccess ? Result.success("更新成功！") : Result.error("更新失败！");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteUserById(@PathVariable Integer id) {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            return Result.error(401, "请先登录");
        }
        if (!isAdmin(currentUser)) {
            return Result.error(403, "仅管理员可删除用户");
        }
        if (Objects.equals(currentUser.getId(), id)) {
            return Result.error(400, "不能删除自己");
        }

        boolean isSuccess = userService.deleteUserById(id);
        return isSuccess ? Result.success("删除成功！") : Result.error("删除失败！");
    }

    @GetMapping("/list")
    public Result<List<Map<String, Object>>> getUserList() {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            return Result.error(401, "请先登录");
        }
        if (!isAdmin(currentUser)) {
            return Result.error(403, "仅管理员可查看用户列表");
        }

        List<Map<String, Object>> list = userService.list().stream()
                .map(this::toSafeUser)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    private boolean isAdmin(User user) {
        return RoleConstants.ROLE_ADMIN.equals(user.getRole())
                || RoleConstants.ROLE_SUPER_ADMIN.equals(user.getRole());
    }

    private Map<String, Object> toSafeUser(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("name", user.getName());
        map.put("age", user.getAge());
        map.put("gender", user.getGender());
        map.put("role", user.getRole());
        map.put("createTime", user.getCreateTime());
        map.put("updateTime", user.getUpdateTime());
        return map;
    }
}