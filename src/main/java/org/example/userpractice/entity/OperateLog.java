package org.example.userpractice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * 对应数据库表 tb_operate_log
 */
@Data
@TableName("tb_operate_log")
public class OperateLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("operate_user_id")
    private Integer operateUserId;

    @TableField("operate_user_name")
    private String operateUserName;

    @TableField("operate_type")
    private String operateType;

    @TableField("request_method")
    private String requestMethod;

    @TableField("request_uri")
    private String requestUri;

    @TableField("request_params")
    private String requestParams;

    @TableField("execute_result")
    private String executeResult;

    @TableField("execute_time")
    private Long executeTime;

    @TableField("error_message")
    private String errorMessage;

    @TableField("operate_time")
    private LocalDateTime operateTime;
}