package com.study.eurekaclient.api

import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

/**
 * .
 * @author yonoel 2021/05/13
 */
@FeignClient("eureka-client-user-service",configuration = [FeignAutoConfiguration::class])
interface UserRemoteClient {
    @GetMapping("/hello")
    fun hello():String
}