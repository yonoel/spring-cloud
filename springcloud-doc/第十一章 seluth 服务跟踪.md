# 第十一章 seluth 服务跟踪

## Spring cloud集成 seluth

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```

日志等级开debug

2021-05-20 11:02:36.472 DEBUG [eureka-client-user-service,f34b48c09a0a64b6,9a58006ccf7e836e] 86369 --- [nio-8081-exec-3] org.apache.tomcat.util.http.Parameters   

2021-05-20 11:02:36.467 DEBUG [eureka-client-article-service,f34b48c09a0a64b6,f34b48c09a0a64b6] 86363 --- [nio-8083-exec-2] org.apache.tomcat.util.http.Parameters   : Set encoding to UTF-8

可以看到特殊的日志，其head是这样的：

[appname,traceid,spanid]

+ appname 服务名称
+ traceid 整个请求的唯一ID，标识整个请求的链路。
+ span ID，基本的工作单元，发起一次远程调用就是一个span

## 整合logstash

### elk介绍

。。。没啥可说的。。

## 整合zipkin

目前看落伍了，skywalking大大超越了zipkin，搞新的吧

