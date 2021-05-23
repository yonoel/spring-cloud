package com.example.transactionmqservice.service

import com.example.transactionmqservice.po.TransactionMessage
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

/**
 * .
 * @author yonoel 2021/05/22
 */
@Service
class MessageService {
    /**
     * 发送消息
     */
    @PostMapping("send")
    fun sendMessage(@RequestBody message: TransactionMessage): Boolean {
        if (check(message)) {
            save(message)
            return true
        }
        return false
    }

    /**
     * 确认消息被消费
     */
    @RequestMapping(method = [RequestMethod.POST], path = ["/confirm/consume"])
    fun confirmReceived(@RequestParam id: Int, @RequestParam receiver: String): Boolean {
        val message = queryById(id)
        return message?.confirm(receiver).let { true }
        return false
    }

    /**
     * 查询最早没有被消费的消息
     */
    @RequestMapping(method=[RequestMethod.GET],path=["/waiting"])
    fun findByWaiting(@RequestParam limit:Int): List<TransactionMessage>{
        return emptyList()
    }

    /**
     * 确认死亡消息
     */
    @RequestMapping(method=[RequestMethod.POST],path=["/confirm/dead"])
    fun confirmDead(@RequestParam id: Int): String{
        return ""
    }

    /**
     * 重发
     */
    @RequestMapping(method=[RequestMethod.GET],path=["/retey/send"])
    fun retry(): String{
        return ""
    }





    private fun save(message: TransactionMessage) {
        TODO("Not yet implemented")
    }

    private fun check(value: TransactionMessage): Boolean {
        TODO("Not yet implemented")
    }



    private fun queryById(id: Int): TransactionMessage? {
        TODO("Not yet implemented")
    }

    /**
     * 增加发送次数
     */
    fun incrSendCount(id: Int) {
        TODO("Not yet implemented")
    }
}