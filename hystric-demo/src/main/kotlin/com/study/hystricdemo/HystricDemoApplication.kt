package com.study.hystricdemo

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HystricDemoApplication

fun main(args: Array<String>) {
//    runApplication<HystricDemoApplication>(*args)
    val context = HystrixRequestContext.initializeContext()
    val future = MyHystrixCollapser("test-abc0").queue()
    val future2 = MyHystrixCollapser("test-abc1").queue()
    print("${future.get()}=${future2.get()}")
//    val execute = ClearCacheHystrixCommand("test").execute()
//    print(execute)
//    ClearCacheHystrixCommand.flushCache("test")
//    val queue = ClearCacheHystrixCommand("test").queue()
//    print(queue.get())
//    val run = MyHystrixCommand("test").execute()
//    print(run)
    context.shutdown()

//    val run = MyHystrixCommand("test").queue()
//    print(run.get())
}
