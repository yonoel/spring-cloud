package com.example.zuuldemo

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext

/**
 * .
 * @author yonoel 2021/05/17
 */
//class LogFilter :ZuulFilter {
//    override fun run(): Any {
//        val request = RequestContext.getCurrentContext().request
//        print("REQUEST::${request.scheme} ${request.remoteAddr}:${request.remotePort}")
//
//        if (request.method == "GET"){
//            val toString = request.parameterMap.map { "${it.key} = ${it.value}" }.joinToString { it }
//            print("uri param : ${toString}")
//        }
//        request.headerNames.toList().forEach { print("head ${it} : ${request.getHeader(it)}") }
//
//
//
//    }
//}