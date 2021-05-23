package com.example.zuuldemo

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * .
 * @author yonoel 2021/05/17
 */
@RestController
class ErrorHandlerController : ErrorController{
    override fun getErrorPath(): String {
        return "/error"
    }

    @RequestMapping("/error")
    fun error(request:HttpServletRequest):ResponseEntity<String>{
        for (name in request.attributeNames) {
            print("name:${name}-value:${request.getAttribute(name)}")
        }
        return ResponseEntity.ok("error")
    }
}