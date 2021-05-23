package com.example.transactionmqservice

import com.example.transactionmqservice.service.ProcessMessageTask
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.concurrent.CountDownLatch

@SpringBootApplication
class TransactionMqServiceApplication{
    val log:Logger = LoggerFactory.getLogger(TransactionMqServiceApplication::class.java)


}

fun main(args: Array<String>) {
    val context = runApplication<TransactionMqServiceApplication>(*args)
    val task = context.getBean(ProcessMessageTask::class.java)
    task.start()
    CountDownLatch(1).await()
}