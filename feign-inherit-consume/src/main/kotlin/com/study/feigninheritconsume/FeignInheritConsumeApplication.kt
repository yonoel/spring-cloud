package com.study.feigninheritconsume

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients(basePackages = ["com.study"])
class FeignInheritConsumeApplication

fun main(args: Array<String>) {
    runApplication<FeignInheritConsumeApplication>(*args)
}
