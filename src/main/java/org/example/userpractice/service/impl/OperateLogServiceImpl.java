package org.example.userpractice.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.userpractice.entity.OperateLog;
import org.example.userpractice.service.OperateLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

// 实现类
@Service
public class OperateLogServiceImpl implements OperateLogService {
    @Override
    public boolean saveBatch(Collection<OperateLog> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<OperateLog> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<OperateLog> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(OperateLog entity) {
        return false;
    }

    @Override
    public OperateLog getOne(Wrapper<OperateLog> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Optional<OperateLog> getOneOpt(Wrapper<OperateLog> queryWrapper, boolean throwEx) {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> getMap(Wrapper<OperateLog> queryWrapper) {
        return Map.of();
    }

    @Override
    public <V> V getObj(Wrapper<OperateLog> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    @Override
    public BaseMapper<OperateLog> getBaseMapper() {
        return null;
    }

    @Override
    public Class<OperateLog> getEntityClass() {
        return null;
    }
    // 必须实现接口的所有方法
    @Async
    @Override
    public void saveOperateLog(OperateLog operateLog) {
        this.save(operateLog);
    }
}
