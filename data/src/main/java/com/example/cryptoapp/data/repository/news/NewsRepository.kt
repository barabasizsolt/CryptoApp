package com.example.cryptoapp.data.repository.news

import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.news.News
import com.example.cryptoapp.data.shared.toModel
import com.example.cryptoapp.data.source.NewsSource

class NewsRepository(private val source: NewsSource) {

    private var cache: MutableList<News>? = null
    private var lastDownloadedPage = 1

    suspend fun getAllNews(refreshType: RefreshType): List<News> = when (refreshType) {
        RefreshType.FORCE_REFRESH -> loadData(1).let { newData ->
            newData.also { cache = it.toMutableList() }
        }
        RefreshType.CACHE_IF_POSSIBLE -> cache.let { currentCache ->
            currentCache ?: loadData(1).let { newData ->
                newData.also { cache = it.toMutableList() }
            }
        }
        RefreshType.NEXT_PAGE -> loadData(lastDownloadedPage + 1).let { newData ->
            val newCache = (cache.orEmpty() + newData)
            cache = newCache.toMutableList()
            newCache
        }
    }

    private suspend fun loadData(page: Int) = source.newsSource.getNews(
        page = page.toString()
    ).body()?.data?.mapNotNull { newsResponse ->
        newsResponse.toModel()
    }?.also {
        lastDownloadedPage = page
    } ?: throw IllegalStateException("Invalid data returned by the server")
}
