package com.example.cryptoapp.data.source

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CryptoRetrofitInstance {
    private const val BASE_URL = "https://api.coinranking.com/v2/"

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    val api : CryptoAPI by lazy {
        retrofit.create(CryptoAPI::class.java)
    }
}