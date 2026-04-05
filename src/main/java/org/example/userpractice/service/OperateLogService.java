package org.example.userpractice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.userpractice.entity.OperateLog;

public interface OperateLogService extends IService<OperateLog> {

    /**
     * 异步保存操作日志
     */
    void saveOperateLog(OperateLog operateLog);
}
