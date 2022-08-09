package com.example.news.home

import com.example.news.home.ArticleViewInfoConverter.convertArticleViewInfo
import com.example.news.model.ArticlesBean
import com.example.news.model.SourceBean
import org.junit.Assert
import org.junit.Test

internal class ArticleViewInfoConverterTest {

    @Test
    fun convertArticleViewInfo_convert() {
        val article = ArticlesBean(SourceBean("1", "2"), "", "title", "", "", "", "", "")
        val viewInfo = convertArticleViewInfo(article)
        Assert.assertEquals(viewInfo.title, "title")
    }
}