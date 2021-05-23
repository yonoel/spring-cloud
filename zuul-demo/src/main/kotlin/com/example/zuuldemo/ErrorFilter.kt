package com.example.zuuldemo

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.springframework.stereotype.Component

/**
 * .
 * @author yonoel 2021/05/17
 */
@Component
class ErrorFilter : ZuulFilter(){
    override fun shouldFilter(): Boolean {
        return true
    }

    override fun run(): Any? {
        val currentContext = RequestContext.getCurrentContext()
        print(currentContext.throwable)
        return null
    }

    override fun filterType(): String {
        return "error"
    }

    override fun filterOrder(): Int {
        return 200
    }
}