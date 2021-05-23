#  第四章 客户端负责均衡Ribbon

## 是什么

目前主流的负载方案分为2种

1. 集中式负载均衡，在消费者和服务方中间提供独立的代理模式进行负载，分别为硬件f5,软件nginx
2. 客户端自己做负载均衡

ribbon是网飞开源的

https://github.com/Netflix/ribbon

特性：

- Load balancing
- Fault tolerance
- Multiple protocol (HTTP, TCP, UDP) support in an asynchronous and reactive model
- Caching and batching

### 模块

- ribbon: APIs that integrate load balancing, fault tolerance, caching/batching on top of other ribbon modules and [Hystrix](https://github.com/netflix/hystrix)
- ribbon-loadbalancer: Load balancer APIs that can be used independently or with other modules
- ribbon-eureka: APIs using [Eureka client](https://github.com/netflix/eureka) to provide dynamic server list for cloud
- ribbon-transport: Transport clients that support HTTP, TCP and UDP protocols using [RxNetty](https://github.com/netflix/rxnetty) with load balancing capability
- ribbon-httpclient: REST client built on top of Apache HttpClient integrated with load balancers (deprecated and being replaced by ribbon module)
- ribbon-example: Examples
- ribbon-core: Client configuration APIs and other shared APIs

### 初步使用

```kotlin
 // init servers
    val servers = listOf(Server("localhost", 8081), Server("localhost", 8083))
    // init loadbalancer
    val loadBalancer =
        LoadBalancerBuilder.newBuilder<Server>().buildFixedServerListLoadBalancer(servers)
    // call 5 times
    for (i in 1..5) {
        val result = LoadBalancerCommand.builder<String>()
            .withLoadBalancer(loadBalancer)
            .build()
            .submit { s: Server ->
                val url = "http://${s.host}:${s.port}/hello"
                println(url)
                try {
                    val response = HttpClient
                        .newHttpClient()
                        .send(
                            HttpRequest.newBuilder(URI.create(url))
                                .GET().build(), HttpResponse.BodyHandlers.ofString()
                        )
                    Observable.just(response.body())
                } catch (e: Exception) {
                    Observable.error(e)
                }
            }
            .toBlocking()
            .first()
        println(result)
```

## 结合resttemplate

### 整合

```kotlin
 @RequestMapping(method=[RequestMethod.GET],path=["/call"])
    fun callData(): String{
        val name = "abc"
        // 直接取回了responseBody
        val forObject =
            restTemplate.getForObject("http://localhost:8081/house/data?name=${name}", HouseInfo::class.java)
        // 取回的是responseEntity
        val forEntity =
            restTemplate.getForEntity("http://localhost:8081/house/data?name=${name}", HouseInfo::class.java)
        // post,delete等类似，exchange可以执行get,post,put,delete
        return ""
    }
```

### resttemplate负载均衡

添加依赖

```xml
 <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency>
```



```kotlin
  	@Bean
    @LoadBalanced
    fun getRestTemplate(): RestTemplate = RestTemplate()
```

### @LoadBalanced 原理

配置了resttemplate的拦截器

```java
//@Autowired放到集合上含义就是获取到所有配置了@LoadBalanced的RestTemplate对象实例。
	@LoadBalanced
	@Autowired(required = false)
	private List<RestTemplate> restTemplates = Collections.emptyList()



//SmartInitializingSingleton就是当所有的singleton的bean都初始化完了之后才会回调这个接口。不过要注意是 4.1 之后才出现的接口。
//效果就是把获得了所有的RestTemplate的定制化器集合customizers，设置到上一步的restTemplates集合中的每个RestTemplate里面
	@Bean
	public SmartInitializingSingleton loadBalancedRestTemplateInitializer(
			final List<RestTemplateCustomizer> customizers) {
		return new SmartInitializingSingleton() {
			@Override
			public void afterSingletonsInstantiated() {
				for (RestTemplate restTemplate : LoadBalancerAutoConfiguration.this.restTemplates) {
					for (RestTemplateCustomizer customizer : customizers) {
						customizer.customize(restTemplate);
					}
				}
			}
		};
	}

```

Tips:

可以利用SmartInitializingSingleton 来实现增强某些类，同功能的还有InitializingBean 接口。

InitializingBean优先于SmartInitializingSingleton。

定了init-method，系统则是先调用afterPropertieSet()方法，然后再调用init-method中指定的方法。

### ribbon api

获取对应的服务信息，用loadbalancedclient.choose("service-name")

### ribbon 饥饿加载

进行服务调用时，若网络不好，可能请求超时，比如修改超时时间，禁用超时等。

ribbon的客户端是在第一次请求的时候才会初始化，所以若超时时间比较短，会导致超时，因此可以开启该模式，避免这个问题。

ribbon.eager-load.enabled=true

ribbon.eager-load.clients=$service-name,$service-name,$service-name

## 负载均衡策略

ribbon 默认轮询。

```
IRule (com.netflix.loadbalancer)
	AbstractLoadBalancerRule (com.netflix.loadbalancer)
		ClientConfigEnabledRoundRobinRule (com.netflix.loadbalancer)
			BestAvailableRule (com.netflix.loadbalancer) 选择最小的并发请求的server，逐个检查server，选择avtiveRequestCount最小的那个
			PredicateBasedRule (com.netflix.loadbalancer)
				ZoneAvoidanceRule (com.netflix.loadbalancer) 使用zonePredicate和AvailabilityPredicate来判断，前一个判断zone性能是否可用，AvailabilityPredicate剔除连接数过多的。
				AvailabilityFilteringRule (com.netflix.loadbalancer) 过滤掉连接失败且标记为circuit tripped 的server，并过滤高并发的server或者使用AvailabilityPredicate来选择。其实就是检查server的状态。
		RoundRobinRule (com.netflix.loadbalancer) 轮询
			WeightedResponseTimeRule (com.netflix.loadbalancer) 根据响应时间分配权重，时间越长权重越低
			ResponseTimeWeightedRule (com.netflix.loadbalancer) 和上面一个一样
		RandomRule (com.netflix.loadbalancer)随机
		RetryRule (com.netflix.loadbalancer)对选定的均衡策略上在加上重试机制，即当选择的某个策略使用后在一个配置时间里选择server不成功，尝试使用subrule。

```

### 自定义策略

实现irule接口，主要在choose方法。

## 配置介绍

1. 禁用eruka，ribbon.eureka.enabled
2. 禁用以后配置server,${server.name}.ribbon.listOfServers
3. 配置策略 ${server.name}.ribbon.NFLoadBalancerRuleClassName=...
4. 超时时间 
   1. connectTimeout
   2. requestTimeout 请求处理
   3. 也可以为每个ribbon客户端配置超时时间 ${server.name}....timeout
5. 并发参数
   1. ribbon.MaxTotalConnections
   2. ribbon.MaxConnectionsPerHost

可以用代码来配置。

@RobbinClient

也可以用配置文件

${server.name}.ribbon.${负载均衡的配置类名称}

## 重试

用nginx负载均衡，若应用是无状态的，可以用滚动发布。

euruka是ap原则，因此，客户端可能获取到的注册表信息不准确，获取到了死亡的实例，所以ribbon就可能请求失败。

1. 可以用retryrule ribbon自带的重试策略
2. Spring实现了重试

```xml
  <dependency>
            <groupId>org.springframework.retry</groupId>
            <artifactId>spring-retry</artifactId>
        </dependency>
```



很明显，resttemplate很麻烦