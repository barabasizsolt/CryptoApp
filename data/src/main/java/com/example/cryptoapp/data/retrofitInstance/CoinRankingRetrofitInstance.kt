package com.example.cryptoapp.data.retrofitInstance

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoinRankingRetrofitInstance(private val baseUrl: String) {
    val coinRankingRetrofitInstance: Retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    }
}
