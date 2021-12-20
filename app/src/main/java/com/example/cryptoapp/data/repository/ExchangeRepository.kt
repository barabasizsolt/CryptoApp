package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.shared.toExchange
import java.lang.IllegalStateException

class ExchangeRepository(private val manager: NetworkManager) {
    suspend fun getAllExchanges(perPage: Int, page: String) =
        manager.exchangeSource.getExchanges(
            perPage = perPage,
            page = page
        ).body()?.mapNotNull { exchangeResponse ->
            exchangeResponse.toExchange()
        } ?: throw IllegalStateException("Invalid data returned by the server")
}
