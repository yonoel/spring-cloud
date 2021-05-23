package com.example.transactionmqservice.po

import java.time.LocalDateTime

/**
 * .
 * @author yonoel 2021/05/22
 */
class TransactionMessage(
    val message:String,
    val queueName:String,
    val senderSystem:String,
    val sendCount:Int,
    /**
     * 最近的发送时间
     */
    val sentDateTime: LocalDateTime,
    /**
     *  发送状态 0 等待消费 1已消费 2死亡
     */
    val sendStatus:Int,
    val deadCount:Int,
    val deadDateTime: LocalDateTime,
    val receivedDateTime: LocalDateTime,
    val receiverSystem: String,

) {
    /**
     * 确认被更新
     */
    fun confirm(receiver: String) {
        TODO("Not yet implemented")
    }

    val id:Int? =null
}