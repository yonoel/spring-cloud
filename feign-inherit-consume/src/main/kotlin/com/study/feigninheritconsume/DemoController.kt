package com.study.feigninheritconsume

import com.study.feigninheritapi.api.UserRemoteClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * .
 * @author yonoel 2021/05/13
 */
@RestController
class DemoController {
    @Autowired
    lateinit var userRemoteClient: UserRemoteClient

    @GetMapping("call")
    fun call():String = userRemoteClient.getName()
}