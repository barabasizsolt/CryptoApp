package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.shared.toNews
import java.lang.IllegalStateException

class NewsRepository(private val manager: NetworkManager) {
    suspend fun getAllNews(page: String) =
        manager.newsSource.getNews(page = page).body()?.data?.mapNotNull { eventResponse ->
            eventResponse.toNews()
        } ?: throw IllegalStateException("Invalid data returned by the server")
}
