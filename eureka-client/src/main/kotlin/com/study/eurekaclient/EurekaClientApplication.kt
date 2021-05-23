package com.study.eurekaclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class EurekaClientApplication

fun main(args: Array<String>) {
    runApplication<EurekaClientApplication>(*args)
}
