package com.example.zuuldemo

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.springframework.context.annotation.Configuration

/**
 * .
 * @author yonoel 2021/05/16
 */
@Configuration
class IpFilter:ZuulFilter() {
    override fun shouldFilter(): Boolean =true
    val black: List<String> = listOf("127.0.0.1")
    override fun run(): Any? {
        val currentContext = RequestContext.getCurrentContext()
//        currentContext.setSendZuulResponse(false)
//        currentContext.set("sendForwardFilter.ran",true)

        val request = currentContext.request
        val remoteAddr = request.remoteAddr
        if (black.contains(remoteAddr)){
            currentContext.responseBody = "{err:1}"
            return null
        }

        return null
    }

    override fun filterType(): String = "pre"

    override fun filterOrder(): Int  =1
}