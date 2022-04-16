package com.example.cryptoapp.data.retrofitInstance

import com.example.cryptoapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoinGekkoRetrofitInstance {
    val coinGekkoRetrofitInstance: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BuildConfig.COINGEKKO_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }
}
