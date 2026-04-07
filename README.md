# 用户管理系统
基于 Spring Boot + MyBatis-Plus + JWT + Redis 的用户管理系统，完全符合大厂开发规范，是完整的单体Spring Boot后端项目。

---

## 🚀 技术栈
|    技术     | 版本    | 说明          |
|-------------|---------|---------------|
| Spring Boot | 2.7.18  | 后端框架       |
| MyBatis-Plus| 3.5.3   | 持久层框架     |
| MySQL       | 8.0     | 关系型数据库   |
| Redis       | 6.0     | 缓存数据库     |
| JWT         |---------| 无状态身份认证 |
| Spring AOP  |---------| 面向切面编程   |
| Knife4j     | 3.0.3   | 在线接口文档   |
| BCrypt      |---------| 密码加密       |
| Maven       |---------| 项目构建工具   |

---

## ✨ 核心功能
### 1. 用户管理模块
- 用户注册（密码 BCrypt 加密存储）
- 用户登录（JWT Token 生成与校验）
- 用户增删改查（完整 CRUD）

### 2. RBAC 权限控制
- 三级角色体系：普通用户、管理员、超级管理员
- 拦截器层粗粒度角色校验
- Controller 层细粒度数据校验
- 符合最小权限原则

### 3. Spring AOP 操作日志审计
- 自动拦截所有 Controller 方法
- 全量操作信息采集：操作人、操作类型、请求参数、执行结果、耗时、异常信息
- 异步日志保存，不阻塞主业务
- 敏感信息脱敏（密码替换为 ***）
- 日志自动存入数据库，可追溯、可审计

### 4. Redis 缓存优化
- 用户详情缓存，接口响应速度提升 100 倍以上
- 缓存更新策略：更新数据库同步删除缓存，保证数据最终一致性
- 缓存空值，解决缓存穿透问题
- 热点数据过期时间：1 小时

### 5. 接口参数校验
- 完整的参数校验：非空校验、长度校验、格式校验、密码复杂度校验
- 全局统一异常处理，返回标准错误格式
- 尽早失败，提升系统健壮性

### 6. 全局统一处理
- 统一返回格式（Result）
- 全局异常处理器
- 接口返回格式完全统一

### 7. Knife4j 在线接口文档
- 自动生成在线接口文档
- 支持在线测试所有接口
- 符合企业前后端联调规范

---

## 🚀 快速开始
### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 1. 克隆仓库
```bash
git clone https://github.com/Xinzhe-666/user-practice.git
cd user-practice
```

### 2. 数据库配置
1. 创建数据库：
```sql
CREATE DATABASE user_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```
2. 执行项目中的 SQL 脚本，创建数据表

### 3. 修改配置文件
打开 `src/main/resources/application.properties`，修改数据库和 Redis 配置：
```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/user_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=你的数据库密码

# Redis配置
spring.redis.host=localhost
spring.redis.port=6379
```

### 4. 启动项目
1. 打开 IDEA，导入项目
2. 等待 Maven 依赖下载完成
3. 运行 `UserPracticeApplication.java` 启动类
4. 看到 `Started UserPracticeApplication in X seconds` 说明启动成功

### 5. 访问接口文档
打开浏览器，访问：`http://localhost:8080/doc.html`

---

## 📂 项目结构
```
user-practice/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org.example.userpractice/
│   │   │       ├── common/          # 通用工具类
│   │   │       │   ├── JwtInterceptor    # JWT拦截器
│   │   │       │   ├── JwtUtil           # JWT工具类
│   │   │       │   ├── RedisUtil         # Redis工具类
│   │   │       │   ├── Result            # 统一返回类
│   │   │       │   ├── RoleConstants     # 角色常量
│   │   │       │   └── UserContext       # 用户上下文
│   │   │       ├── config/          # 配置类
│   │   │       │   ├── GlobalExceptionHandler  # 全局异常处理器
│   │   │       │   ├── Knife4jConfig          # Knife4j配置
│   │   │       │   ├── MyBatisPlusConfig      # MyBatis-Plus配置
│   │   │       │   ├── MyMetaObjectHandler    # 自动填充配置
│   │   │       │   ├── OperateLogAspect       # 操作日志切面
│   │   │       │   ├── RedisConfig            # Redis配置
│   │   │       │   └── WebConfig              # Web配置
│   │   │       ├── controller/      # 控制器层
│   │   │       │   └── UserController        # 用户控制器
│   │   │       ├── entity/          # 实体类
│   │   │       │   ├── OperateLog            # 操作日志实体
│   │   │       │   └── User                  # 用户实体
│   │   │       ├── mapper/          # 数据访问层
│   │   │       │   ├── OperateLogMapper      # 操作日志Mapper
│   │   │       │   └── UserMapper            # 用户Mapper
│   │   │       ├── service/         # 业务逻辑层
│   │   │       │   ├── impl/                # 实现类
│   │   │       │   │   ├── OperateLogServiceImpl
│   │   │       │   │   └── UserServiceImpl
│   │   │       │   ├── OperateLogService
│   │   │       │   └── UserService
│   │   │       └── UserPracticeApplication  # 启动类
│   │   └── resources/
│   │       └── application.properties  # 配置文件
└── pom.xml  # Maven依赖
```

---

## 🌟 项目亮点
1. 完全符合企业开发规范：代码结构清晰，分层合理，可维护性强
2. 覆盖后端核心技能：JWT认证、AOP切面、Redis缓存、参数校验、全局异常处理等
3. 解决企业级问题：缓存穿透、数据一致性、安全合规、异步处理等
4. 完整的在线接口文档：支持在线测试，符合前后端联调规范
5. 可直接写进校招简历：项目描述、技术栈、功能特性、亮点说明完整

---

** 📝 测试账号
|    角色   | 用户名      | 密码         |
|-----------|-------------|--------------|
| 超级管理员 | super_admin | Super@123456 |
| 管理员     | admin       | Admin@123456 |
| 普通用户   | test_user   | Test@123456  |
 
---

📄 许可证
本项目仅供学习使用。

---

👤 作者
Xinzhe Li
