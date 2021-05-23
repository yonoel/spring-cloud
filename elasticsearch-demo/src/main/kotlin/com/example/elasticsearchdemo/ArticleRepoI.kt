package com.example.elasticsearchdemo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * .
 * @author yonoel 2021/05/22
 */
@Repository
interface ArticleRepoI : CrudRepository<Article, Int> {

    fun findByTitleContaining(content: String): List<Article>
}