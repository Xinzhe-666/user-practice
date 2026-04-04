package org.example.userpractice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库表 tb_user
 */
@Data
@TableName("tb_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @TableField("name")
    private String name;

    @NotBlank(message = "密码不能为空")
    @TableField("password")
    private String password;

    @TableField("age")
    private Integer age;

    @TableField("gender")
    private String gender;

    @TableField("role")
    private String role;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}