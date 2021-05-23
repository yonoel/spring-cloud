package com.example.hystrixfeigndemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication
@EnableHystrix
class HystrixFeignDemoApplication {
    @Bean
    fun getRest(): RestTemplate = RestTemplate()
}

fun main(args: Array<String>) {
    runApplication<HystrixFeignDemoApplication>(*args)
}
