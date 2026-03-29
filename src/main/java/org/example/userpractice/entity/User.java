package org.example.userpractice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import javax.validation.constraints.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

// 绑定你的数据库表tb_user
@TableName("tb_user")
public class User {

    // 主键自增，和表的id字段对应
    @TableId(type = IdType.AUTO)
    private Integer id;

    // 姓名：不能为空，长度不能超过50
    @NotBlank(message = "用户姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    // 年龄：必须大于等于0，小于等于120
    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 120, message = "年龄不能超过120岁")
    private Integer age;

    // 性别：只能是「男」或「女」
    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^[男女]$", message = "性别只能是男或女")
    private String gender;

    // 密码：不能为空，长度不能少于6位
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6位")
    private String password;

    // 创建时间：仅在新增数据时自动填充
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时间：新增和修改数据时都自动填充
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 逻辑删除标记
    @TableLogic
    private Integer deleted;

    // 以下是所有字段的getter和setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}