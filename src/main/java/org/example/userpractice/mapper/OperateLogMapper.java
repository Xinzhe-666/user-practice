package org.example.userpractice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.userpractice.entity.OperateLog;

@Mapper
public interface OperateLogMapper extends BaseMapper<OperateLog> {
}