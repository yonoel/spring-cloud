package com.study.feigninheritprovide

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class FeignInheritProvideApplication

fun main(args: Array<String>) {
    runApplication<FeignInheritProvideApplication>(*args)
}
