package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.constant.NewsConstant.DEFAULT_PAGE
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.news.News
import com.example.cryptoapp.data.shared.toNews

class NewsRepository(private val manager: NetworkManager) {

    private var cache: MutableList<News>? = null
    private var lastDownloadedPage = DEFAULT_PAGE

    suspend fun getAllNews(refreshType: RefreshType): List<News> = when (refreshType) {
        RefreshType.FORCE_REFRESH -> loadData(DEFAULT_PAGE).let { newData ->
            newData.also { cache = it.toMutableList() }
        }
        RefreshType.CACHE_IF_POSSIBLE -> cache.let { currentCache ->
            currentCache ?: loadData(DEFAULT_PAGE).let { newData ->
                newData.also { cache = it.toMutableList() }
            }
        }
        RefreshType.NEXT_PAGE -> loadData(lastDownloadedPage + 1).let { newData ->
            val newCache = (cache.orEmpty() + newData).toMutableList()
            cache = newCache
            newCache
        }
    }

    private suspend fun loadData(page: Int) = manager.newsSource.getNews(
        page = page.toString()
    ).body()?.data?.mapNotNull { newsResponse ->
        newsResponse.toNews()
    }?.also {
        lastDownloadedPage = page
    } ?: throw IllegalStateException("Invalid data returned by the server")
}
