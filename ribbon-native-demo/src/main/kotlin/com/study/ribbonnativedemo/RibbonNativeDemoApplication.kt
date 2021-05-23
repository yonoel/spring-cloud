package com.study.ribbonnativedemo

import com.netflix.loadbalancer.LoadBalancerBuilder
import com.netflix.loadbalancer.Server
import com.netflix.loadbalancer.reactive.LoadBalancerCommand
import com.netflix.loadbalancer.reactive.ServerOperation
import org.springframework.boot.autoconfigure.SpringBootApplication
import rx.Observable
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

//@SpringBootApplication
class RibbonNativeDemoApplication {

}

fun main(args: Array<String>) {
//    runApplication<RibbonNativeDemoApplication>(*args)
    // init servers
    val servers = listOf(Server("localhost", 8081), Server("localhost", 8083))
    // init loadbalancer
    val loadBalancer =
        LoadBalancerBuilder.newBuilder<Server>().buildFixedServerListLoadBalancer(servers)
    // call 5 times
    for (i in 1..5) {
        val result = LoadBalancerCommand.builder<String>()
            .withLoadBalancer(loadBalancer)
            .build()
            .submit { s: Server ->
                val url = "http://${s.host}:${s.port}/hello"
                println(url)
                try {
                    val response = HttpClient
                        .newHttpClient()
                        .send(
                            HttpRequest.newBuilder(URI.create(url))
                                .GET().build(), HttpResponse.BodyHandlers.ofString()
                        )
                    Observable.just(response.body())
                } catch (e: Exception) {
                    Observable.error(e)
                }
            }
            .toBlocking()
            .first()
        println(result)

    }

}


