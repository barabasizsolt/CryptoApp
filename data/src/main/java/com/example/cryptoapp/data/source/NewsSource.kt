package com.example.cryptoapp.data.source

import com.example.cryptoapp.data.retrofitInstance.CoinGekkoRetrofitInstance
import com.example.cryptoapp.data.service.NewsService

class NewsSource(private val retrofitInstance: CoinGekkoRetrofitInstance) {

    val newsSource: NewsService by lazy {
        retrofitInstance.coinGekkoRetrofitInstance.create(NewsService::class.java)
    }
}
