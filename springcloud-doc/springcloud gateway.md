# springcloud gateway

## 介绍

目的：旨在提供一种简单有效，统一的api路由管理

特点：依赖springboot和webflux，基于netty，无法在传统的servlet容器工作，不能构建war包

核心概念

+ route 由ID，目标uri，断言，过滤器组成，当请求到达网关时，通过gateway handler mapping 通过断言进行路由匹配
+ predicate
+ filter

## 工作原理

filter只有pre和post

1. 客户端向gateway发出请求，gateway handler mapping 通过断言去找对应的gateway web handler
2. gateway web handler，若存在，则触发过滤器

## 快速上手

### 创建项目

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

启动就好了

### 路由规则

```yml
spring:
  cloud:
    gateway:
      routes:
        - id: path_route
          uri: http://cxytiandi.com
          predicates:
            - Path=/course,/blog/**
```

### 整合eureka

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

```yaml
- id: user-service
  uri: lb://user-service
  predicates:
    - Path=/user-service/**
```

lb开头代表从注册中心获取服务，后面是服务名

### 整合eureka 默认路由

```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
```

这样就和zuul访问服务名一样

比如访问http://网关地址/服务名称（大写）/**

```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
```

这样就能小写，和zuul相同

## 路由断言工厂

1. path
2. Query 一个必须的参数和对应的值的表达式
3. method 匹配对应的方法
4. header 请求头和对应的值的表达式
5. after,before,between 时间判定ZonedDateTime类型
6. cookie 断言，the cookie `name` and a `regexp`
7. host 断言，a list of host name `patterns`
8. RemoteAddr,takes a list (min size 1) of `sources`,such as `192.168.0.1/16`
9. Weight, takes two arguments: `group` and `weight`

### 自定义断言工厂

```java
@Component
@Slf4j
public class CheckAuthRoutePredicateFactory extends
        AbstractRoutePredicateFactory<CheckAuthRoutePredicateFactory.Config> {
    public CheckAuthRoutePredicateFactory() {
        super(Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return serverWebExchange -> {
            log.info("进入断言");
            if (config.name.equals("demo")) {
                return true;
            }
            return false;
        };
    }

    @Getter
    @Setter
    public static class Config {
        private String name;
    }
}
```

```yaml
- id: customer_route
  uri: http://cxytiandi.com
  predicates:
    - name: CheckAuth
      args:
        name: demo
```

## spring cloud gate 过滤器工厂

### 过滤器使用

gatewayfilter factory 是spring cloud gateway提供的过滤器工厂，有许多内置的过滤器。

1. addRequestHeader,takes a `name` and `value` parameter. 
2. addRequestParameter, takes a `name` and `value` parameter.
3. addResponseHeader,takes a `name` and `value` parameter.
4. DedupeResponse , takes a `name` parameter and an optional `strategy` parameter. `name` can contain a space-separated list of header names
5. CircuitBreaker,断路器an optional `fallbackUri` parameter. Currently, only `forward:` schemed URIs are supported.
6. FallbackHeaders，The `FallbackHeaders` factory lets you add Spring Cloud CircuitBreaker execution exception details in the headers of a request forwarded to a `fallbackUri` in an external application, as in the following scenario:
7. MapRequestHeader，The `MapRequestHeader` `GatewayFilter` factory takes `fromHeader` and `toHeader` parameters. It creates a new named header (`toHeader`)
8. PrefixPath，takes a single `prefix` parameter
9. PreserveHostHeader，
10. RequestRateLimiter限流器
11. RedirectTo 重定向
12. RemoveRequestHeader
13. RemoveResponseHeader
14. RemoveRequestParameter
15. RewritePath，takes a path `regexp` parameter and a `replacement` parameter. 
16. RewriteLocationResponseHeader
17. RewriteResponseHeader
18. SaveSession
19. SecureHeaders
20. SetPath
21. SetRequestHeader
22. SetResponseHeader
23. SetStatus
24. StripPrefix
25. Retry
26. RequestSize
27. SetRequestHostHeader
28. ModifyRequestBody
29. ModifyResponseBody
30.  Token Relay，总算有这个了。。

## 全局过滤器

globalfilter

## 实战

### 限流器

我用redis实现过。。基本就是复制本身redis限流器的代码

```jade
@Override
@SuppressWarnings("unchecked")
public Mono<Response> isAllowed(String routeId, String id) {
    if (!this.initialized.get()) {
        throw new IllegalStateException("RedisRateLimiter is not initialized");
    }

    CustomRedisRateLimiter.Config routeConfig = loadConfiguration(routeId);

    // How many requests per second do you want a user to be allowed to do?
    int replenishRate = routeConfig.getReplenishRate();

    // How much bursting do you want to allow?
    int burstCapacity = routeConfig.getBurstCapacity();

    try {
        List<String> keys = getKeys(id);

        // The arguments to the LUA script. time() returns unixtime in seconds.
        List<String> scriptArgs = Arrays.asList(replenishRate + "",
                burstCapacity + "", Instant.now().getEpochSecond() + "", "1");
        // allowed, tokens_left = redis.eval(SCRIPT, keys, args)
        Flux<List<Long>> flux = this.redisTemplate.execute(this.script, keys,
                scriptArgs);
        // .log("redisratelimiter", Level.FINER);
        return flux.onErrorResume(throwable ->
                {
                    log.info(throwable);
                    return Flux.just(Arrays.asList(1L, -1L));
                }
        )
                .reduce(new ArrayList<Long>(), (longs, l) -> {
                    longs.addAll(l);
                    return longs;
                }).map(results -> {
                    boolean allowed = results.get(0) == 1L;
                    Long tokensLeft = results.get(1);
                    Response response = new Response(allowed,
                            getHeaders(routeConfig, tokensLeft));
                    // 这里就说明可能超限了。。
                    if (!allowed) {
                        notifyDeveloper(null, "超限了", routeConfig);
                    }
                    return response;
                });
    } catch (Exception e) {

        /*
         * We don't want a hard dependency on Redis to allow traffic. Make sure to set
         * an alert so you know if this is happening too much. Stripe's observed
         * failure rate is 0.01%.
         */
        if (routeConfig.needNotify) {
            notifyDeveloper(e, null, routeConfig);
        }
    }
    return Mono.just(new Response(true, getHeaders(routeConfig, -1L)));
}
```

要注意的是，原先的代码里，发生异常是被捕获了，导致不知道具体情况，这里也要开异常通知。

至于限流的key是啥，看具体需求了，可以基于ip

redis的限流器还有一个问题，就是缓存击穿，需要布隆过滤器搞一下。

### fallback

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

```properties
- id: user-service
  uri: lb:user-service
  predicates:
    - Path=/user-service/**
  filters:
    - name: Hystrix
      args:
        name: fallbackcmd
        fallbackUri: forward:/callback
```

### 跨域配置

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins: "*"
            exposedHeaders:
              - content-type
            allowedHeaders:
              - content-type
            allowedMethod:
              - GET
              - OPTIONS
              - PUT
              - DELETE
              - POST
```

### 统一异常处理

```java
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {
    public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        int code = 500;
        final Throwable error = super.getError(request);
        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            code = 404;
        }
        return response(code, this.buildMessage(request, error));
    }


    private String buildMessage(ServerRequest request, Throwable ex) {
        final StringBuilder builder = new StringBuilder("Failed to handler request[");
        builder.append(request.methodName())
                .append(" ")
                .append(request.uri())
                .append("]");
        if (ex != null) {
            builder.append(": ").append(ex.getMessage());
        }
        return builder.toString();
    }

    private Map<String, Object> response(int code, String buildMessage) {
        final HashMap<String, Object> map = new HashMap<>(3);
        map.put("code", code);
        map.put("message", buildMessage);
        map.put("data", null);
        return map;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        int code = (int) errorAttributes.get("code");
        return HttpStatus.valueOf(code);
    }


}
```

```java
@Configuration
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class})
public class ErrorHandlerConfiguration {
    private final ServerProperties serverProperties;
    private final ApplicationContext applicationContext;
    private final ResourceProperties resourceProperties;
    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public ErrorHandlerConfiguration(ServerProperties serverProperties, ApplicationContext applicationContext, ResourceProperties resourceProperties, ObjectProvider<List<ViewResolver>> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        this.serverProperties = serverProperties;
        this.applicationContext = applicationContext;
        this.resourceProperties = resourceProperties;
        this.viewResolvers = viewResolvers.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes){
        final JsonExceptionHandler jsonExceptionHandler = new JsonExceptionHandler(
                errorAttributes,
                this.resourceProperties,
                this.serverProperties.getError(),
                this.applicationContext
        );
        jsonExceptionHandler.setViewResolvers(this.viewResolvers);
        jsonExceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
        jsonExceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        return jsonExceptionHandler;
    }
}
```

注意ObjectProvider。。。。springboot4.3

### 重试机制

重试过滤器