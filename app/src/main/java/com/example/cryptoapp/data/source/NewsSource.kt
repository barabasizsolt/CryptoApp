package com.example.cryptoapp.data.source

import com.example.cryptoapp.data.service.NewsService
import com.example.cryptoapp.data.retrofitInstance.CoinGekkoRetrofitInstance

class NewsSource(private val retrofitInstance: CoinGekkoRetrofitInstance) {

    val newsSource: NewsService by lazy {
        retrofitInstance.coinGekkoRetrofitInstance.create(NewsService::class.java)
    }
}