package com.study.eurekaconsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication(scanBasePackages = ["com.study"])
@EnableFeignClients
class EurekaConsumerApplication {

    @Bean
    @LoadBalanced
    fun getRestTemplate(): RestTemplate = RestTemplate()
}





fun main(args: Array<String>) {
    runApplication<EurekaConsumerApplication>(*args)
}




