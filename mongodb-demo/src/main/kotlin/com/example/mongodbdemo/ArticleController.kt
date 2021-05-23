package com.example.mongodbdemo

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.CriteriaDefinition
import org.springframework.data.mongodb.core.query.Query
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

/**
 * .
 * @author yonoel 2021/05/22
 */
@RestController
class ArticleController(val template: MongoTemplate) {
    @GetMapping(path = ["test"])
    fun batchAdd(){
        for (num in 0..10){
            val article = Article(
                title = "mongoTemplate的基本使用",
                author = "demo",
                tags = listOf("java", "mongodb"),
                visitCount = num,
                addTime = LocalDateTime.now()
            )
            template.save(article)
        }
    }
    @GetMapping("test2")
    fun query(){
        val query = Query.query(Criteria.where("author").`is`("demo"))
        val find = template.find(query, Article::class.java)
        val findAll = template.findAll(Article::class.java)
    }
}