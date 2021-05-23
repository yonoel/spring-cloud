package com.example.hystrixfeigndemo

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

/**
 * .
 * @author yonoel 2021/05/15
 */
@RestController
class DemoController(val restTemplate: RestTemplate,val userRemoteClient: UserRemoteClient) {
    @RequestMapping(method = [RequestMethod.GET], path = ["/callhello"])
    @HystrixCommand(
        fallbackMethod = "defaultCallHello",
        commandProperties = [HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")]
    )
    fun demo(): String? {
        return "ok"
//        return restTemplate.getForObject("http://localhost:8088/houst/hello", String::class.java)
//        return userRemoteClient.hello()
    }

    fun defaultCallHello(): String = "defaultCallHello-fail"
}