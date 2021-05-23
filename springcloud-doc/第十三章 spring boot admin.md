# 第十三章 spring boot admin

监控和管理软件，基于springboot actuator

## 使用方法

服务端：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-server</artifactId>
</dependency>
```

```java
@SpringBootApplication
@EnableAdminServer
public class SpringBoodAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoodAdminApplication.class, args);
    }

}
```

配置端口，就能访问了

客户端

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-client</artifactId>
</dependency>
```

```properties
server.port=9092
spring.boot.admin.client.url=http://localhost:9091
management.endpoints.web.exposure.include=*
```

然后就能在server端提供的web上看到服务了。。还能看日志。。

若要开安全。。引入security即可

## 集成eureka

每个服务都要配置admin地址是不是很烦

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

## 监控预警服务

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

```properties
spring.mail.host=smtp.qq.com
spring.mail.username=402032357@qq.com
spring.mail.password=123456
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

spring.boot.admin.notify.mail.to=402032357@qq.com
spring.boot.admin.notify.mail.from=402032357@qq.com
```

丁丁发送

集成abstractstatuschangenotifier，返回bean

remindingnotifier 配置发送频率