package com.example.cryptoapp.data.source
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CoinGekkoRetrofitInstance {
    private const val BASE_URL = "https://api.coingecko.com/api/v3/"

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    val CoinGekkoApi: CoinGekkoAPI by lazy {
        retrofit.create(CoinGekkoAPI::class.java)
    }
}
