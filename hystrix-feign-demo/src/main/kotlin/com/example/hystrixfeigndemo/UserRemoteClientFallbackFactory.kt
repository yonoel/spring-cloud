package com.example.hystrixfeigndemo

import feign.hystrix.FallbackFactory
import org.springframework.stereotype.Component
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * .
 * @author yonoel 2021/05/15
 */
class UserRemoteClientFallbackFactory : FallbackFactory<UserRemoteClient> {
    override fun create(p0: Throwable?): UserRemoteClient
    {
        return object : UserRemoteClient {
            override fun hello(): String {
                return "hello"
            }
        }
    }

}