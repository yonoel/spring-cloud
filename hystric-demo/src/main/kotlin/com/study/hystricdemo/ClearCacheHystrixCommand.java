package com.study.hystricdemo;


import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;

/**
 * .
 *
 * @author yonoel 2021/05/13
 */
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
