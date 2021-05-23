package com.example.transactionmqservice.service

import com.example.transactionmqservice.po.TransactionMessage
import org.redisson.api.RedissonClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.*

/**
 * .
 * @author yonoel 2021/05/23
 */
@Service
class ProcessMessageTask(val messageService: MessageService, val redission: RedissonClient) {
    val log: Logger = LoggerFactory.getLogger(ProcessMessageTask::class.java)

    private val fixedPool: ExecutorService = Executors.newFixedThreadPool(10)
    private val semaphore: Semaphore = Semaphore(20)
    fun start() {
        Thread {
            while (true) {
                val lock = redission.getLock("message-task")
                try {
                    lock.lock()
                    var sleepTime = process()
                    if (sleepTime > 0) {
                        TimeUnit.MILLISECONDS.sleep(sleepTime.toLong())
                    }
                }finally {
                    lock.unlock()
                }

            }
        }.start()
}

    private fun process(): Int {
        var sleepTime = 10000;
        val waiting = messageService.findByWaiting(5000)
        if (waiting.size == 5000){
            sleepTime = 0
        }
        val latch = CountDownLatch(waiting.size)
        for (message in waiting) {
            semaphore.acquire()
            fixedPool.execute{
                try {
                    doProcess(message)
                }finally {
                    semaphore.release()
                    latch.countDown()
                }
            }
        }
        latch.await()
        return sleepTime
    }

    private fun doProcess(message: TransactionMessage) {
        // 检查消息是否满足死亡条件
        if (message.sendCount >message.deadCount){
            messageService.confirmDead(message.id!!)
            return
        }
        // 时间间隔是否满足
        if (message.sentDateTime?.plusMinutes(1).isBefore(LocalDateTime.now())){
            log.info("发送具体消息")
            // 生产者发送消息进mq
            messageService.incrSendCount(message.id!!)
        }
    }
}