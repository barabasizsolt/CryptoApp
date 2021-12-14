package com.example.cryptoapp.data

import com.example.cryptoapp.data.service.CryptoService
import com.example.cryptoapp.data.service.EventService
import com.example.cryptoapp.data.service.ExchangeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkManager {
    private val cryptoRetrofit by lazy {
        Retrofit.Builder().baseUrl(CRYPTO_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    private val exchangeRetrofit by lazy {
        Retrofit.Builder().baseUrl(GEKKO_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    private val eventRetrofit by lazy {
        Retrofit.Builder().baseUrl(GEKKO_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }

    val cryptoSource: CryptoService by lazy {
        cryptoRetrofit.create(CryptoService::class.java)
    }

    val exchangeSource: ExchangeService by lazy {
        exchangeRetrofit.create(ExchangeService::class.java)
    }

    val eventSource: EventService by lazy {
        eventRetrofit.create(EventService::class.java)
    }

    companion object {
        private const val CRYPTO_BASE_URL = "https://api.coinranking.com/v2/"
        private const val GEKKO_BASE_URL = "https://api.coingecko.com/api/v3/"
    }
}
