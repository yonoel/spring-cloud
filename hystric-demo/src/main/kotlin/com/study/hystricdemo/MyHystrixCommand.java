package com.study.hystricdemo;


import java.util.concurrent.TimeUnit;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * .
 *
 * @author yonoel 2021/05/13
 */
public class MyHystrixCommand extends HystrixCommand<String> {
    private final String name;

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

    @Override
    protected String getFallback() {
        return "超时，调用失败";
    }

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
}
