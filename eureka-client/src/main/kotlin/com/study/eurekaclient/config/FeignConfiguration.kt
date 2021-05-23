package com.study.eurekaclient.config

import feign.Contract
import feign.Logger
import feign.Request
import feign.auth.BasicAuthRequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * .
 * @author yonoel 2021/05/13
 */
@Configuration
class FeignConfiguration {
//    @Bean
//    fun getContract(): Contract = feign.Contract.Default()

    @Bean
    fun getLoggerLevel(): Logger.Level = Logger.Level.FULL
    @Bean
    fun getOption():Request.Options = Request.Options(5000,10000)

//    @Bean
//    fun getBasicAuthRequestInterceptor(): BasicAuthRequestInterceptor = BasicAuthRequestInterceptor("user", "password")
}