package com.study.eurekaconsumer.controller

import com.study.eurekaclient.api.UserRemoteClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
/**
 * .
 * @author yonoel 2021/05/12
 */
@RestController
class ArticleController() {
    @Autowired
    lateinit var restTemplate: RestTemplate
    @Autowired
    lateinit var userRemoteClient: UserRemoteClient

    @GetMapping("/callhello")
    fun callHello(): String? =
        userRemoteClient.hello();
//        restTemplate.getForObject("http://localhost:8081/hello", String::class.java)
//        restTemplate.getForObject("http://eureka-client-user-service/hello", String::class.java)


}