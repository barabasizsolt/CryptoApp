package com.example.cryptoapp.data.source

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoRetrofitInstance {
    private val baseUrl = "https://api.coinranking.com/v2/"

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
    }

    val api: CryptoAPI by lazy {
        retrofit.create(CryptoAPI::class.java)
    }
}
