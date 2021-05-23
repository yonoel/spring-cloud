package com.study.feigninheritprovide.controller

import com.study.feigninheritapi.api.UserRemoteClient
import org.springframework.web.bind.annotation.RestController

/**
 * .
 * @author yonoel 2021/05/13
 */
@RestController
class DemoController:UserRemoteClient {
    override fun getName(): String {
        return "ceshi"
    }
}