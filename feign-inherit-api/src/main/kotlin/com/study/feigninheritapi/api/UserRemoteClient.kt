package com.study.feigninheritapi.api

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * .
 * @author yonoel 2021/05/13
 */
@FeignClient("feign-inherit-provide")
interface UserRemoteClient {
    @RequestMapping(method = [RequestMethod.GET], path = ["/user/name"])
    fun getName(): String
}