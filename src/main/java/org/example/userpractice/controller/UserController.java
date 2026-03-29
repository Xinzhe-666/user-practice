package org.example.userpractice.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.userpractice.common.Result;
import org.example.userpractice.entity.User;
import org.example.userpractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    // 注入Service层
    @Autowired
    private UserService userService;

    // 1. 查询所有用户
    @GetMapping("/list")
    public Result<List<User>> getUserList() {
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

    // 3. 添加用户（参数校验注解@Valid）
    @PostMapping("/add")
    public Result<String> addUser(@RequestBody @Valid User user) {
        boolean isSuccess = userService.addUser(user);
        if (isSuccess) {
            return Result.success("添加成功！新用户ID：" + user.getId());
        }
        return Result.error("添加失败！用户名已存在");
    }

    // 4. 修改用户
    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody @Valid User user) {
        boolean isSuccess = userService.updateUser(user);
        if (isSuccess) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败！用户不存在");
    }

    // 5. 删除用户
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Integer id) {
        boolean isSuccess = userService.deleteUserById(id);
        if (isSuccess) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败！用户不存在");
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
        IPage<User> userPage = userService.getUserByPage(pageNum, pageSize, name, minAge, maxAge, gender);
        return Result.success(userPage);
    }
}