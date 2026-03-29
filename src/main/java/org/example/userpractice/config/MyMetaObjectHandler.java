package org.example.userpractice.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

// @Component注解：把这个处理器交给Spring管理，MyBatis-Plus会自动识别并生效
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    // 插入数据时的填充规则：对应FieldFill.INSERT
    @Override
    public void insertFill(MetaObject metaObject) {
        // 给createTime填充当前系统时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        // 给updateTime填充当前系统时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    // 更新数据时的填充规则：对应FieldFill.INSERT_UPDATE
    @Override
    public void updateFill(MetaObject metaObject) {
        // 给updateTime刷新为当前修改的时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}