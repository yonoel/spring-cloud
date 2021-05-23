package com.study.springresttemplate

import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

/**
 * .
 * @author yonoel 2021/05/13
 */
@RestController
class HouseController(val restTemplate: RestTemplate) {
    @RequestMapping(method = [RequestMethod.GET], path = ["/house/data"])
    fun getHouseInfo(@RequestParam name: String): HouseInfo {
        return HouseInfo(name)
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/house/data/{name}"])
    fun getData2(@PathVariable name: String): HouseInfo {
        return HouseInfo(name)
    }

    @RequestMapping(method=[RequestMethod.GET],path=["/call"])
    fun callData(): String{
        val name = "abc"
        // 直接取回了responseBody
        val forObject =
            restTemplate.getForObject("http://localhost:8081/house/data?name=${name}", HouseInfo::class.java)
        // 取回的是responseEntity
        val forEntity =
            restTemplate.getForEntity("http://localhost:8081/house/data?name=${name}", HouseInfo::class.java)
        // post,delete等类似，exchange可以执行get,post,put,delete
        return ""
    }
}

class HouseInfo(val name: String) {

}
