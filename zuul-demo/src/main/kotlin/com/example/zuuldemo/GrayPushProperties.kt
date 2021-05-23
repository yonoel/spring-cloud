package com.example.zuuldemo

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 * .
 * @author yonoel 2021/05/22
 */
@Configuration
class GrayPushProperties(
    /**
     *  服务名称
     */
    @Value("\${gray.server}")
    val servers: String,
    /**
     * 用户
     */
    @Value("\${gray.user}")
    val users: String
)