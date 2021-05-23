package com.example.hystrixfeigndemo

import org.springframework.stereotype.Component


/**
 * .
 * @author yonoel 2021/05/15
 */
@Component
class UserRemoteClientFallback:UserRemoteClient{
    override fun hello(): String  ="feign-fail"
}