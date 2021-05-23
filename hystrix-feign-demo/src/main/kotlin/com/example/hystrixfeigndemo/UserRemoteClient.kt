package com.example.hystrixfeigndemo

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

/**
 * .
 * @author yonoel 2021/05/15
 */
@FeignClient(value = "eureka-client-user-service", fallback = UserRemoteClientFallback::class)
interface UserRemoteClient {
    @GetMapping("/user/hello")
    fun hello(): String
}