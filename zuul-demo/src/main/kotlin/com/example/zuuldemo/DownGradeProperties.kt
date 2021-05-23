package com.example.zuuldemo

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

/**
 * 降级配置类.
 * @author yonoel 2021/05/22
 */
@Configuration
class DownGradeProperties(
    @Value("\${downGradeService:default}")
    /**
     * 服务降级的属性，用,号隔开
     */
    val downGradeService: String
)