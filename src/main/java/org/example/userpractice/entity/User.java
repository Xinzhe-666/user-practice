package org.example.userpractice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@TableName("tb_user") // 对应你的数据库表名
public class User {

    @TableId(type = IdType.AUTO) // 主键自增
    private Integer id;

    // 用户名：非空，长度2-10位
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 10, message = "用户名长度必须在2-10位之间")
    private String name;

    // 年龄：1-150之间
    @Min(value = 1, message = "年龄不能小于1岁")
    @Max(value = 150, message = "年龄不能超过150岁")
    private Integer age;

    // 性别：只能是男/女
    @Pattern(regexp = "^[男女]$", message = "性别只能填写男或女")
    private String gender;

    // 密码：非空，长度6-20位
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;

    // 用户角色：对应RoleConstants中的常量
    private String role;
}