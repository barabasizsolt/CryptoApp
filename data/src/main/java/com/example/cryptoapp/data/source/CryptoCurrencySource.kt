package com.example.cryptoapp.data.source

import com.example.cryptoapp.data.retrofitInstance.CoinRankingRetrofitInstance
import com.example.cryptoapp.data.service.CryptoCurrencyService

class CryptoCurrencySource(private val retrofitInstance: CoinRankingRetrofitInstance) {

    val cryptoCurrencySource: CryptoCurrencyService by lazy {
        retrofitInstance.coinRankingRetrofitInstance.create(CryptoCurrencyService::class.java)
    }
}
