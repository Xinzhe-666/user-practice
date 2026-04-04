package org.example.userpractice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.userpractice.entity.OperateLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperateLogMapper extends BaseMapper<OperateLog> {
}