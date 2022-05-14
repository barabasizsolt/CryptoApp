package com.example.cryptoapp.data.repository.exchange

import com.example.cryptoapp.data.shared.toModel
import com.example.cryptoapp.data.source.ExchangeSource
import java.lang.IllegalStateException

class ExchangeHistoryRepository(private val source: ExchangeSource) {

    suspend fun getExchangeHistory(id: String, days: Int) =
        source.exchangeSource.getExchangeHistory(
            id = id,
            days = days
        ).body()?.mapNotNull { history -> history.toModel() } ?: throw IllegalStateException("Invalid data returned by the server")
}

