# 第六章 hystrix服务容错处理

微服务架构存在多个可直接调用的服务，服务若在调用时出现故障导致连锁效应，让整个系统不可用，称之为雪崩。

## Hystrix

是网飞的熔断保护中间件，通过hystrixcommand隔离服务。

Hystrix如何实现:

使用命令模式将所有对外部服务（或依赖关系）的调用包装在HystrixCommand或HystrixObservableCommand对象中，并将该对象放在单独的线程中执行；
每个依赖都维护着一个线程池（或信号量），线程池被耗尽则拒绝请求（而不是让请求排队）。
记录请求成功，失败，超时和线程拒绝。
服务错误百分比超过了阈值，熔断器开关自动打开，一段时间内停止对该服务的所有请求。
请求失败，被拒绝，超时或熔断时执行降级逻辑。
近实时地监控指标和配置的修改。

### 简单使用

导入依赖

```xml
<dependency>
    <groupId>com.netflix.hystrix</groupId>
    <artifactId>hystrix-core</artifactId>
    <version>1.5.18</version>
</dependency>
```

```java
public class MyHystrixCommand extends HystrixCommand<String> {
    private final String name;

    public MyHystrixCommand(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("MyGroup"));

        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        return String.format("%s:%s",this.name,Thread.currentThread().getName());
    }
}
```

```kotlin
fun main(args: Array<String>) {
//    runApplication<HystricDemoApplication>(*args)
    val run = MyHystrixCommand("test").execute()
    print(run)

//    val run = MyHystrixCommand("test").queue()
//    print(run.get())
}
```

输出结果并不是test:main,而是test:MyGroup-1.构造函数里的组名变成了线程的名称

### 回退支持

通过增加执行时间的方式模拟超时失败

```java
public class MyHystrixCommand extends HystrixCommand<String> {
    private final String name;

    public MyHystrixCommand(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("MyGroup"));
        this.name = name;
    }

    @Override
    protected String getFallback() {
        return "超时，调用失败";
    }

    @Override
    protected String run() throws Exception {
        TimeUnit.SECONDS.sleep(10);
        return String.format("%s:%s", this.name, Thread.currentThread().getName());
    }
}
```

再次执行main函数，打印失败了的提示语。

### 信号量策略

```java
 public MyHystrixCommand(String name) {
//        super(HystrixCommandGroupKey.Factory.asKey("MyGroup"));
        super(HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("MyGroup"))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(
                                HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE
                        )
                )
        );
        this.name = name;
    }
```

通过这个名称就可以确定当前是现场隔离还是信号量隔离

### 线程池策略

```java
 public MyHystrixCommand(String name) {
//        super(HystrixCommandGroupKey.Factory.asKey("MyGroup"));
        super(HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("MyGroup"))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionIsolationStrategy(
//                                        HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE
                                        HystrixCommandProperties.ExecutionIsolationStrategy.THREAD
                                )
                ).andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties.Setter()
                                .withCoreSize(10)
                                .withMaxQueueSize(100)
                                .withMaximumSize(100)
                ));
        this.name = name;
    }
```

### 结果缓存

提供了方法级别的缓存，重写函数。

```java
 @Override
    protected String getCacheKey() {
        return String.valueOf(this.name);
    }

    @Override
    protected String run() throws Exception {
//        TimeUnit.SECONDS.sleep(10);
        System.err.println("get data");
        return String.format("%s:%s", this.name, Thread.currentThread().getName());
    }
```

err的输出只会有一次～～说明走了缓存。

### 清除缓存

```java
public class ClearCacheHystrixCommand extends HystrixCommand<String> {
    private final String name;
    private final static HystrixCommandKey KEY =
    HystrixCommandKey.Factory.asKey("MyKey");

    public ClearCacheHystrixCommand(String name) {
        super(HystrixCommand.Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey("MyGroup")
        ).andCommandKey(KEY));
        this.name = name;
    }

    public static void flushCache(String name){
        HystrixRequestCache.getInstance(KEY, HystrixConcurrencyStrategyDefault.getInstance())
                .clear(name);

    }

    @Override
    protected String getFallback() {
        return "调用失败";
    }

    @Override
    protected String getCacheKey() {
        return String.valueOf(this.name);
    }

    @Override
    protected String run() throws Exception {
        System.err.println("get data");
        return String.format("%s:%s", this.name, Thread.currentThread().getName());
    }
}

public void  main(args: Array<String>) {
    val context = HystrixRequestContext.initializeContext()
    val execute = ClearCacheHystrixCommand("test").execute()
    print(execute)
    ClearCacheHystrixCommand.flushCache("test")
    val queue = ClearCacheHystrixCommand("test").queue()
    print(queue.get())

}

```

### 合并请求

支持将多个请求合并一个请求，节省网络开销。

```java
public class MyHystrixCollapser extends HystrixCollapser<List<String>,String,String> {
    private final String name;

    public MyHystrixCollapser(String name) {
        this.name = name;
    }

    @Override
    public String getRequestArgument() {
        return name;
    }

    @Override
    protected HystrixCommand<List<String>> createCommand(Collection<CollapsedRequest<String, String>> collapsedRequests) {
        return new BatchCommand(collapsedRequests);
    }

    @Override
    protected void mapResponseToRequests(List<String> batchResponse, Collection<CollapsedRequest<String, String>> collapsedRequests) {
        int count = 0;
        for (CollapsedRequest<String, String> collapsedRequest : collapsedRequests) {
            collapsedRequest.setResponse(batchResponse.get(count++));
        }
    }
}
public class BatchCommand extends HystrixCommand<List<String>> {
    private final Collection<HystrixCollapser.CollapsedRequest<String, String>>  requests;
    public BatchCommand(Collection<HystrixCollapser.CollapsedRequest<String, String>> collapsedRequests) {
        super(Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey("ExampleGroup")
        )
        .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueFoeKey"))
        );
        this.requests = collapsedRequests;
    }

    @Override
    protected List<String> run() throws Exception {
        System.out.println("真正执行请求。。。。。。。。。");
        return requests.stream().map(request->String.format("result %s",request.getArgument())).collect(Collectors.toList());
    }
}


```

```kotlin
fun main(args: Array<String>) {

    val context = HystrixRequestContext.initializeContext()
    val future = MyHystrixCollapser("test-abc").queue()
    val future2 = MyHystrixCollapser("test-abc").queue()
    print("${future.get()}=${future2.get()}")

    context.shutdown()

}
```

最后通过输出可以发现，2个请求是归并到一起顺序执行。

mapResponse是按序放，其实都能乱序。。结果也能改返回类型

## springcloud里的使用

## 简单使用

```kotlin
@SpringBootApplication
@EnableHystrix
class HystrixFeignDemoApplication {
    @Bean
    fun getRest(): RestTemplate = RestTemplate()
}

@RestController
class DemoController(val restTemplate: RestTemplate) {
    @RequestMapping(method = [RequestMethod.GET], path = ["/callhello"])
    @HystrixCommand(fallbackMethod = "defaultCallHello")
    fun demo(): String? {
        return restTemplate.getForObject("http://localhost:8088/houst/hello", String::class.java)
    }

    fun defaultCallHello(): String = "fail"
}
```

然后调用失败就会返回fail

## 配置介绍

## 结合feign

### 1 fallbak

```kotlin
@FeignClient(value = "eureka-client-user-service", fallback = UserRemoteClientFallback::class)
interface UserRemoteClient {
    @GetMapping("/user/hello")
    fun hello(): String
}
@Component
class UserRemoteClientFallback:UserRemoteClient{
    override fun hello(): String  ="fail"
}
@RestController
class DemoController(val restTemplate: RestTemplate,val userRemoteClient: UserRemoteClient) {
    @RequestMapping(method = [RequestMethod.GET], path = ["/callhello"])
    fun demo(): String? {
        return userRemoteClient.hello()
    }

    fun defaultCallHello(): String = "fail"
}
```

配置文件要打开断路器

具体版本的配置名称可能不一样。

然后就会触发fail

### 2 failfactory

```kotlin
@Component
class UserRemoteClientFallbackFactory : FallbackFactory<UserRemoteClient> {
    override fun create(p0: Throwable?): UserRemoteClient
    {
        return object : UserRemoteClient {
            override fun hello(): String {
                return "hello"
            }
        }
    }

}
@FeignClient(value = "eureka-client-user-service", fallback = UserRemoteClientFallback::class
,fallbackFactory = UserRemoteClientFallbackFactory::class)
interface UserRemoteClient {
    @GetMapping("/user/hello")
    fun hello(): String
}
```

kotlin 匿名类需要object关键字

这里我测试下来，第一次的fallback用的hysrixcommand的配置，后来都是feign的fallback

### 禁用hystrix

配置关闭

## hystrix监控

hystrix会实时累计关于command的执行信息，有2个前提条件：

1. 有actuator依赖

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   ```

2. 有hystrix依赖，并开启

访问http://localhost:8080/actuator/hystrix.stream 会看到一直在ping

因为还没激活command执行

调用后就会有数据：

```json
data: {"type":"HystrixCommand","name":"demo","group":"DemoController","currentTime":1621087577173,"isCircuitBreakerOpen":false,"errorPercentage":0,"errorCount":0,"requestCount":0,"rollingCountBadRequests":0,"rollingCountCollapsedRequests":0,"rollingCountEmit":0,"rollingCountExceptionsThrown":0,"rollingCountFailure":0,"rollingCountFallbackEmit":0,"rollingCountFallbackFailure":0,"rollingCountFallbackMissing":0,"rollingCountFallbackRejection":0,"rollingCountFallbackSuccess":0,"rollingCountResponsesFromCache":0,"rollingCountSemaphoreRejected":0,"rollingCountShortCircuited":0,"rollingCountSuccess":0,"rollingCountThreadPoolRejected":0,"rollingCountTimeout":0,"currentConcurrentExecutionCount":0,"rollingMaxConcurrentExecutionCount":0,"latencyExecute_mean":0,"latencyExecute":{"0":0,"25":0,"50":0,"75":1,"90":1,"95":1,"99":1,"99.5":1,"100":1},"latencyTotal_mean":0,"latencyTotal":{"0":0,"25":0,"50":0,"75":1,"90":1,"95":1,"99":1,"99.5":1,"100":1},"propertyValue_circuitBreakerRequestVolumeThreshold":20,"propertyValue_circuitBreakerSleepWindowInMilliseconds":5000,"propertyValue_circuitBreakerErrorThresholdPercentage":50,"propertyValue_circuitBreakerForceOpen":false,"propertyValue_circuitBreakerForceClosed":false,"propertyValue_circuitBreakerEnabled":true,"propertyValue_executionIsolationStrategy":"THREAD","propertyValue_executionIsolationThreadTimeoutInMilliseconds":1000,"propertyValue_executionTimeoutInMilliseconds":1000,"propertyValue_executionIsolationThreadInterruptOnTimeout":true,"propertyValue_executionIsolationThreadPoolKeyOverride":null,"propertyValue_executionIsolationSemaphoreMaxConcurrentRequests":10,"propertyValue_fallbackIsolationSemaphoreMaxConcurrentRequests":10,"propertyValue_metricsRollingStatisticalWindowInMilliseconds":10000,"propertyValue_requestCacheEnabled":true,"propertyValue_requestLogEnabled":true,"reportingHosts":1,"threadPool":"DemoController"}
```

## 整合dashboard

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>
```

```kotlin
@SpringBootApplication
@EnableHystrixDashboard
class HystrixDashboardDemoApplication
```

访问http://host:port/hystrix

然后输入，上面监控具体服务的域名即：http://localhost:8080/actuator/hystrix.stream 

就会把json数据可视化

## trubine聚合集群数据

hystix只能监控单个节点，弱鸡。

这个也是个垃圾，只能监控单个服务的集群。

要去eureka拉服务信息，然后去找配置的监控服务做监控。

若想对多个服务的集群进行监控，那trubine也得做集群

