package com.example.cryptoapp.data.source

import com.example.cryptoapp.data.retrofitInstance.CoinGekkoRetrofitInstance
import com.example.cryptoapp.data.service.ExchangeService

class ExchangeSource(private val retrofitInstance: CoinGekkoRetrofitInstance) {

    val exchangeSource: ExchangeService by lazy {
        retrofitInstance.coinGekkoRetrofitInstance.create(ExchangeService::class.java)
    }
}
