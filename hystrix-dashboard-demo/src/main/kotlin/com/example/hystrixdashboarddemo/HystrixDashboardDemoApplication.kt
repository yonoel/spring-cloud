package com.example.hystrixdashboarddemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard
import org.springframework.cloud.netflix.turbine.EnableTurbine

@SpringBootApplication
@EnableHystrixDashboard
@EnableTurbine
@EnableDiscoveryClient
class HystrixDashboardDemoApplication

fun main(args: Array<String>) {
    runApplication<HystrixDashboardDemoApplication>(*args)
}
