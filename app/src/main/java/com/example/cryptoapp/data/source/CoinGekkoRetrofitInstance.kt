package com.example.cryptoapp.data.source
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoinGekkoRetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    val coinGekkoApi: CoinGekkoAPI by lazy {
        retrofit.create(CoinGekkoAPI::class.java)
    }

    companion object {
        private const val BASE_URL = "https://api.coingecko.com/api/v3/"
    }
}
