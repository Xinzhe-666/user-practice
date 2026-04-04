package org.example.userpractice.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.userpractice.entity.OperateLog;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * 操作日志Service接口
 * 继承IService，自动拥有基础的业务方法
 */
public interface OperateLogService extends IService<OperateLog> {
    // 必须实现接口的所有方法
    @Async
    // 异步保存日志，不阻塞主业务
    void saveOperateLog(OperateLog operateLog);
}

