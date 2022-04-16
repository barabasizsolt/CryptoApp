package com.example.cryptoapp.data.source

import com.example.cryptoapp.data.service.CryptoCurrencyService
import com.example.cryptoapp.data.retrofitInstance.CoinRankingRetrofitInstance

class CryptoCurrencySource(private val retrofitInstance: CoinRankingRetrofitInstance) {

    val cryptoCurrencySource: CryptoCurrencyService by lazy {
        retrofitInstance.coinRankingRetrofitInstance.create(CryptoCurrencyService::class.java)
    }
}