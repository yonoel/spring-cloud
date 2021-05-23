package com.example.elasticsearchdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ElasticsearchDemoApplication

fun main(args: Array<String>) {
    runApplication<ElasticsearchDemoApplication>(*args)
}
