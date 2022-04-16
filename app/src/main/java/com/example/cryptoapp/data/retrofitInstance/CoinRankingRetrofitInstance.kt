package com.example.cryptoapp.data.retrofitInstance

import com.example.cryptoapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoinRankingRetrofitInstance {
    val coinRankingRetrofitInstance: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BuildConfig.COINRANKING_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }
}