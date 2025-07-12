# Spring Boot Kiki 系统安全与优化说明

## 1. 全局异常处理与数据校验

### 已实现的异常处理类型：
- **参数校验异常** (`@Validated` 注解校验失败)
- **参数类型转换异常** (路径参数类型错误)
- **JSON解析异常** (请求体格式错误)
- **业务异常** (`RuntimeException`)
- **权限异常** (`UnauthorizedException`)
- **数据完整性异常** (外键约束、唯一约束等)
- **数据不存在异常** (`EmptyResultDataAccessException`)
- **限流异常** (`RateLimitException`)
- **系统异常** (其他未捕获的异常)

### 使用示例：
```java
// 在Controller方法上使用参数校验
@PostMapping("/register")
public ResponseMessage<RegistrationResponseDTO> register(@Validated @RequestBody RegistrationDTO dto) {
    // 业务逻辑
}

// 在DTO中使用校验注解
public class RegistrationDTO {
    @NotBlank(message = "姓名不能为空")
    private String realName;
    
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;
}
```

## 2. 接口权限校验/鉴权

### 权限校验机制：
- 基于Token的简单权限校验
- 管理端接口需要携带有效的Authorization头
- 公开接口无需权限校验

### 需要权限的接口：
- 会议管理：新增、编辑、删除会议
- 会议详情管理：标准定制、技术培训、工具研发详情
- 行业动态管理：新增、编辑、删除、状态管理

### 使用示例：
```bash
# 调用管理端接口需要携带Token
curl -X POST http://localhost:8088/api/conferences \
  -H "Authorization: Bearer admin-token-2024" \
  -H "Content-Type: application/json" \
  -d '{"title":"新会议","description":"会议描述"}'
```

### 有效Token（测试用）：
- `admin-token-2024` - 管理员权限
- `user-token-2024` - 用户权限

## 3. 接口限流

### 限流注解使用：
```java
@RateLimit(time = 60, count = 5, type = RateLimit.LimitType.IP)
public ResponseMessage<RegistrationResponseDTO> register(...) {
    // 60秒内每个IP最多5次注册请求
}

@RateLimit(time = 60, count = 10, type = RateLimit.LimitType.USER_ID)
public ResponseMessage<UserBehaviorDTO> like(...) {
    // 60秒内每个用户最多10次点赞操作
}
```

### 限流类型：
- **IP限流**：按客户端IP地址限流
- **用户ID限流**：按用户ID限流
- **全局限流**：全局接口限流

### 已添加限流的接口：
- 会议注册接口：60秒内每个IP最多5次
- 行业动态点赞：60秒内每个用户最多10次
- 行业动态收藏：60秒内每个用户最多10次

## 4. 单元测试

### 测试文件位置：
- `src/test/java/com/kiki/springboot_kiki/ConferenceRegistrationServiceTest.java`

### 测试覆盖：
- 会议注册成功场景
- 重复注册异常场景
- 会议不存在异常场景
- 注册状态检查

### 运行测试：
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=ConferenceRegistrationServiceTest
```

## 5. 配置说明

### 安全配置：
- `SecurityConfig.java` - 拦截器配置
- `AuthInterceptor.java` - 权限校验逻辑
- `RateLimitInterceptor.java` - 限流逻辑

### 异常处理：
- `GlobalExceptionHandlerAdvice.java` - 全局异常处理器
- `UnauthorizedException.java` - 权限异常
- `RateLimitException.java` - 限流异常

## 6. 生产环境建议

### 权限校验优化：
- 使用JWT替代简单Token
- 添加Token过期时间
- 实现用户角色权限系统
- 使用Redis存储Token

### 限流优化：
- 使用Redis替代内存存储
- 实现分布式限流
- 添加限流监控和告警
- 支持动态限流配置

### 异常处理优化：
- 添加异常监控和日志聚合
- 实现异常告警机制
- 优化异常信息，避免敏感信息泄露

### 测试优化：
- 增加集成测试
- 添加性能测试
- 实现自动化测试流程
- 提高测试覆盖率

## 7. 接口调用示例

### 公开接口（无需Token）：
```bash
# 获取会议列表
curl http://localhost:8088/api/conferences?page=0&size=10

# 获取会议详情
curl http://localhost:8088/api/conferences/1

# 检查注册状态
curl http://localhost:8088/api/conferences/1/check-registration?phone=13800000000
```

### 管理端接口（需要Token）：
```bash
# 新增会议
curl -X POST http://localhost:8088/api/conferences \
  -H "Authorization: Bearer admin-token-2024" \
  -H "Content-Type: application/json" \
  -d '{"title":"新会议","description":"会议描述"}'

# 编辑会议
curl -X PUT http://localhost:8088/api/conferences/1 \
  -H "Authorization: Bearer admin-token-2024" \
  -H "Content-Type: application/json" \
  -d '{"title":"修改后的会议标题"}'

# 删除会议
curl -X DELETE http://localhost:8088/api/conferences/1 \
  -H "Authorization: Bearer admin-token-2024"
```

### 限流测试：
```bash
# 快速连续调用注册接口测试限流
for i in {1..10}; do
  curl -X POST http://localhost:8088/api/conferences/register \
    -H "Content-Type: application/json" \
    -d '{"conferenceId":1,"realName":"测试用户","phone":"13800000000"}'
  echo "第$i次调用"
done
```

## 8. 监控和日志

### 日志级别：
- ERROR：异常信息
- WARN：警告信息
- INFO：业务信息
- DEBUG：调试信息

### 关键日志：
- 权限校验失败
- 限流触发
- 业务异常
- 系统异常

### 监控指标：
- 接口调用次数
- 异常发生次数
- 限流触发次数
- 响应时间统计 