# SpringCloud 入门实战与进阶

## 概述

单体应用难以满足日趋增长的用户量，数据量，就会扩展其中某个模块。当子系统越来越多，系统间的维护十分困难。

互联网公司对应用的要求：

1. 高并发
2. 高可用
3. 高扩展



阿里巴巴的Dubbo解决了服务之间的调用问题。

为啥要转向SpringCloud呢？

1. 社区支持
   1. Spring社区更新多
   2. Dubbo停止维护
2. 关注内容
   1. 关注整个服务架构的方方面面，快速集成，成本低。
   2. Dubbo针对服务治理
3. 性能问题
   1. Dubbo基于netty，spring基于http，性能比不过
   2. 可以接受http的损耗

## Springcloud是什么

是一系列框架的集合，利用springboot的开发便利性，简化了分布式系统的基础设施开发，如服务注册，发现，配置中心，消息总线，负载均衡，断路器，数据监控等。

主要贡献来自于Netflix oss

## 模块介绍

+ Eureka:服务注册中心，服务管理
+ Ribbon:基于客户端的负载均衡
+ Hystrix:容错框架，防止服务雪崩
+ Feign:服务调用框架
+ Zuul:网关
+ Config:配置中心

地址：https://spring.io/projects/spring-cloud

