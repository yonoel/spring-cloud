package com.example.elasticsearchdemo

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType

/**
 * .
 * @author yonoel 2021/05/22
 */
@Document(indexName = "article_index")
class Article (
    @Field(type = FieldType.Keyword)
    val sid:String,
    @Field(type = FieldType.Keyword)
    val title:String,
    @Field(type = FieldType.Keyword)
    val url:String,
    @Field(type = FieldType.Keyword)
    val content:String
    ){
    @Id
    @Field(type = FieldType.Integer)
    var id:String? = null
}