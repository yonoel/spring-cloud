package com.example.mongodbdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class MongodbDemoApplication{


}

fun main(args: Array<String>) {
    runApplication<MongodbDemoApplication>(*args)
}
