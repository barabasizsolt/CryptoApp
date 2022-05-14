package com.example.cryptoapp.data.repository.exchange

import com.example.cryptoapp.data.shared.toModel
import com.example.cryptoapp.data.source.ExchangeSource
import java.lang.IllegalStateException

class ExchangeDetailRepository(private val source: ExchangeSource) {

    suspend fun getExchangeDetails(id: String) =
        source.exchangeSource.getExchangeDetails(id = id).body()?.toModel() ?: throw IllegalStateException("Invalid data returned by the server")
}