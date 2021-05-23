package com.example.zuuldemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@SpringBootApplication
@EnableZuulProxy
class ZuulDemoApplication

fun main(args: Array<String>) {
    runApplication<ZuulDemoApplication>(*args)
}
