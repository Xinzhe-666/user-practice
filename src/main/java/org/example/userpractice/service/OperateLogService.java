package org.example.userpractice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.userpractice.entity.OperateLog;

public interface OperateLogService extends IService<OperateLog> {
    void saveOperateLog(OperateLog operateLog);
}