package com.example.zuuldemo

import com.netflix.ribbon.Ribbon
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.springframework.beans.factory.annotation.Autowired

/**
 * .
 * @author yonoel 2021/05/22
 */

class GrayPushFilter(@Autowired val properties: GrayPushProperties): ZuulFilter() {
    override fun shouldFilter(): Boolean {
        return RequestContext.getCurrentContext()["isSuccess"] as Boolean ?: true
    }

    override fun run(): Any? {
        val context = RequestContext.getCurrentContext()
        // auth filter 验证成功，之后设置的用户ID
        val uid = context.zuulRequestHeaders["uid"] as String
        RibbonFilterContextHolder
//        val serviceId = context["serviceId"]
//        if (serviceId != null && properties != null){
//            if (properties.downGradeService.split(",").contains(serviceId)) {
//                context.setSendZuulResponse(false)
//                context.set("isSuccess",false)
//                context.responseBody = "{\"msg\":\"服务降级中\",\"code\":200 }"
//                return null
//            }
//        }
        return null
    }

    override fun filterType(): String = "route"

    override fun filterOrder(): Int = 4
}