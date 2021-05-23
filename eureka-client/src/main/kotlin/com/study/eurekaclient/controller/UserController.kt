package com.study.eurekaclient.controller

import com.study.eurekaclient.api.UserRemoteClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * .
 * @author yonoel 2021/05/12
 */
@RestController
class UserController:UserRemoteClient{
    @GetMapping("hello")
    override fun hello(): String = "helloabcder"
}