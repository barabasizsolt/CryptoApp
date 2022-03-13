package com.example.cryptoapp.data.manager

import com.example.cryptoapp.data.service.CategoriesService
import com.example.cryptoapp.data.service.CryptoService
import com.example.cryptoapp.data.service.ExchangeService
import com.example.cryptoapp.data.service.NewsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManager {
    private val coinRankingRetrofitInstance by lazy {
        Retrofit.Builder().baseUrl(CRYPTO_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    private val coinGekkoRetrofitInstance by lazy {
        Retrofit.Builder().baseUrl(GEKKO_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    val cryptoSource: CryptoService by lazy {
        coinRankingRetrofitInstance.create(CryptoService::class.java)
    }

    val categorySource: CategoriesService by lazy {
        coinGekkoRetrofitInstance.create(CategoriesService::class.java)
    }

    val exchangeSource: ExchangeService by lazy {
        coinGekkoRetrofitInstance.create(ExchangeService::class.java)
    }

    val newsSource: NewsService by lazy {
        coinGekkoRetrofitInstance.create(NewsService::class.java)
    }

    companion object {
        private const val CRYPTO_BASE_URL = "https://api.coinranking.com/v2/"
        private const val GEKKO_BASE_URL = "https://api.coingecko.com/api/v3/"
    }
}
