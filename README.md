# 配置：

```
server:
  port: 8081
spring:
  application:
    name: SimpleConfiguration

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://{ip}:3306/simple_configuration?useSSL=false&serverTimezone=UTC
    username: {username}
    password: {password}
    type: com.alibaba.druid.pool.DruidDataSource

  data:
    redis:
      host: {ip}
      port: 6379
      password: {password}
      database: 0
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
          max-wait: 100ms

    mongodb:
      host: {ip}
      port: 27017
      database: simple_configuration
      username: {username}
      password: {password}
      authentication-database: admin





  jackson:
    default-property-inclusion: non_null

mybatis-plus:
  type-aliases-package: com.mata.pojo

logging:
  level:
    com.mata: debug

```

