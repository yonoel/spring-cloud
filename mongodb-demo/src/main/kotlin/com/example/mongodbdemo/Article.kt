package com.example.mongodbdemo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

/**
 * .
 * @author yonoel 2021/05/22
 */
@Document(collection = "article_info")
data class Article(

    @Field("title")
    val title:String,
    @Field("author")
    val author:String,
    @Field("tags")
    val tags:List<String>,
    @Field("visitCount")
    val visitCount:Int,
    @Field("addTime")
    val addTime:LocalDateTime
){
    @Id
    var id:String? = null
}
