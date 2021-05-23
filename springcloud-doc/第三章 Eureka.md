# 第三章 Eureka

注册中心，基于rest的服务，提供java的客户端。

基于ap构建，保证ap就用，若cp则zk。

## 服务器端

导入依赖,用注解开启,这两个配置一定要

```properties
# 配置server名称
eureka.client.register-with-eureka=false
#不用去抓取信息注册表
eureka.client.fetch-registry=false


```

## 客户端

导入依赖

```properties
spring.application.name=eureka-client-user-service
server.port=8081
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.appname=${spring.application.name}
eureka.instance.prefer-ip-address=true
eureka.instance.instance-id=${spring.application.name}:${spring.cloud.client.ip-address}:${server.port}

```

其实只要有依赖就行了。。就自行注册，除非想关闭。

## 消费者

配置和客户端一样，消费客户端的某个接口

```kotlin
		@Bean
    @LoadBalanced
    fun getRestTemplate(): RestTemplate = RestTemplate()
```

即可自动构造LoadBalancedclient

## 安全认证

直接使用

```xml
 <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
```

配置文件里写下用户名，密码，拆箱即用。

```properties
spring.security.user.name=yonoel
spring.security.user.password=123456
```

增加Security的配置类。



## 高可用搭建

配置一样，用defaultzone注册

```properties
eureka.client.service-url.defaultZone=http://yonoel@123456${eureka.instance.hostname}:8762/eureka/,http://yonoel@123456${eureka.instance.hostname}:8761/eureka/,http://yonoel@123456${eureka.instance.hostname}:8763/eureka/

```



## 常用配置

### 自我保护

server会保护其注册表内的信息，不删除注册表里的信息。

Eureka通过“自我保护模式”来解决这个问题——当Eureka Server节点在短时间内丢失过多客户端时（可能发生了网络分区故障），那么这个节点就会进入自我保护模式。一旦进入该模式，Eureka Server就会保护服务注册表中的信息，不再删除服务注册表中的数据（也就是不会注销任何微服务）。当网络故障恢复后，该Eureka Server节点会自动退出自我保护模式。

综上，自我保护模式是一种应对网络异常的安全保护措施。它的架构哲学是宁可同时保留所有微服务（健康的微服务和不健康的微服务都会保留），也不盲目注销任何健康的微服务。使用自我保护模式，可以让Eureka集群更加的健壮、稳定。



PS：感觉自我保护就是一个坑。推荐关闭该功能。

### 自定义Instance-Id

默认是eureka.instance.instance-id=${spring.cloud.client.hostname}:${spring.application.name}:${server.port}

即-----主机名，服务名，端口

可以换成ip

${spring.application.name}:${spring.cloud.client.ip-address}:${server.port}

即-----服务名，ip，端口

但是跳转时还可能跳转服务，而不是IP，导致找不到，可以配置

preferIpAdress=true

### 跳转链接

在eureka的页面点击名称时，会自动跳转。

可以自定义配置

eureka.instance.status-page-url=http://baidu.com

### 快速删除已经失效的服务

服务器端：

1. 关闭自我保护
2. eureka.server.eviction-interval-timer-in-ms=5000

客户端:

1. Health check.enable=true
2. Lease-renwal-interval-in-second=5 发送心跳的时间差
3. Lease-expiration-duration-in-second=5 收到消息后的有效期，时间到了就删除了

客户端主动拉取数据

## 扩展

### restful api

eureka作为注册中心，其本质是存储了每个客户端的信息，ribbon转发的时候会获取注册中心的服务列表，然后根据对应的路由规则选择一个服务给feign来进行调用。

eureka提供了rest的api提供调用

### 元数据

eureka的数据分2种，分别是框架定义的标准元数据，和用户自定义的元数据。

标准元数据包括，主机名，ip，端口，状态，健康检查等信息。

可以通过自定义元数据的方式来实现灰度发布。

### 使用client

可以用client来获取server的数据，或者可以用discoverclient（Spring封装的feign的客户端）

### 健康检查

### 服务上下线服务

eureka事件，注意集群情况下所有机器都会监听到。