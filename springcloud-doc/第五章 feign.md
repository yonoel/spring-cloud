# 第五章 feign

java项目里调用接口的方式

1. Http-client -apache
2. okhttp
3. httpurlconnection jdk自带的，很差
4. Resttemplate spring
5. htppclient jdk11

## 使用feign调用api

### springcloud集成

引入依赖

```xml
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```

```java
@EnableFeignClients
@FeignClient("eureka-client-user-service")
```

定义feignclient的接口，有2种策略

1. 接口单独定义，controller实现，在调用的客户端也实现，从而接口共用
2. 单独创建一个api client公共项目，基于约定，每写一个接口，写一个对应的client，打包成公共的jar，其他项目引用

## 配置介绍

### 日志

feign有4个级别日志

1. none
2. basic只输出请求方法url，状态码，执行时间
3. headers 2+headers
4. full

在定义feignclient的时候，定义配置类

```kotlin
@Bean
fun getLoggerLevel(): Logger.Level = Logger.Level.FULL
@FeignClient("eureka-client-user-service",configuration = [FeignAutoConfiguration::class])
    
```



同时在配置文件里修改日志级别.

logging.level.${class}=level(常规的几种级别)

### 契约

spring在feign上拓展，使得feign支持mvc的注解，若想用原生的注解来定义客户端也可以。要配置契约类。

```kotlin
@Bean
    fun getContract(): Contract = feign.Contract.Default()
```

注意用了这个，spring的就不支持了。

### 认证

比如basic认证

```kotlin
@Bean
fun getBasicAuthRequestInterceptor(): BasicAuthRequestInterceptor = BasicAuthRequestInterceptor("user", "password")
```

自定义认证，就是实现拦截器。

### 超时配置

```kotlin
@Bean
fun getOption():Request.Options = Request.Options(5000,10000)
```

### 客户端组件

feign默认使用jdk的urlconnection，可以加别的依赖，然后关闭feign的httpclient，启用配置。

### gzip压缩

### 编码器配置

比如gson，jaxb，jackson

配置decoder和encoder即可。

### 使用配置文件来定义

### 接口特性

feign的特性就是让服务的接口定义抽离出来，作为公共依赖。但是一定要有注册中心，来获取url。

### 参数构造

如果是post请求，实现类参数上也得加@RequestBody

## 脱离Spring cloud

### 原生使用

GitHub:https://github.com/OpenFeign/feign

使用的注解是@RuquestLine

### 构建对象

通过buidler模式构建接口的代理对象，设置具体配置。

Feign.target(apiType,url)