package com.example.elasticsearchdemo

import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * .
 * @author yonoel 2021/05/22
 */
@RestController
@EnableElasticsearchRepositories
class TestController(val repo: ArticleRepoI) {
    @RequestMapping(method=[RequestMethod.GET],path=["test"])
    fun test(): String{
        for (num in 0..10){
            val article = Article(
                title = "es的基本使用",
                sid = "demo",
                content = "demo",
                url = "www.baidu.com"
            )
            repo.save(article)
        }

        val findAll = repo.findAll()
        print(findAll)
        val containing = repo.findByTitleContaining("demo")
        print(containing)
        return ""
    }
}