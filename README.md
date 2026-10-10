# LoggingX Java

LoggingX 是一个基于 Spring Boot 4.1、Spring Security 7 和 Java 25 的智能日志分析平台后端。

## 模块结构

```text
loggingx-java
├── loggingx-common       # 通用响应模型与应用异常
├── loggingx-dao          # 数据访问接口及当前内存实现
├── loggingx-thirdparty   # 外部系统适配器
├── loggingx-auth         # 注册、登录、JWT 与安全配置
├── loggingx-biz          # 用户及日志分析业务服务
└── loggingx-web          # Spring Boot 启动入口和 HTTP API
```

模块依赖方向保持单向：`web -> (auth, biz, thirdparty) -> dao -> common`。

## 启动

需要 Java 25 和 Maven 3.6.3 或更高版本：

```shell
mvn spring-boot:run -pl loggingx-web -am
```

生产环境必须通过环境变量设置至少 32 字符的 JWT 密钥：

```shell
LOGGINGX_SECURITY_JWT_SECRET=your-long-random-secret
```

当前用户数据由内存仓储保存，服务重启后会清空；后续接入数据库时，只需要替换 `UserAccountRepository` 的实现。

## API 示例

注册并获取令牌：

```http
POST /api/auth/register
Content-Type: application/json

{"username":"demo","password":"password123"}
```

登录：

```http
POST /api/auth/login
Content-Type: application/json

{"username":"demo","password":"password123"}
```

访问受保护资源：

```http
GET /api/users/me
Authorization: Bearer <accessToken>
```

健康检查：`GET /actuator/health`
