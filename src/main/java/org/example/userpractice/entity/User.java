package org.example.userpractice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@TableName("tb_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名：非空，长度2-20
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20之间")
    private String name;

    /**
     * 密码：非空，必须包含大小写字母、数字、特殊字符，长度8-20
     */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "密码必须包含大小写字母、数字、特殊字符，长度8-20")
    private String password;

    /**
     * 年龄：非空，1-120
     */
    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄最小为1")
    @Max(value = 120, message = "年龄最大为120")
    private Integer age;

    /**
     * 性别：非空
     */
    @NotBlank(message = "性别不能为空")
    private String gender;

    /**
     * 角色：普通用户、管理员、超级管理员
     */
    private String role;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}