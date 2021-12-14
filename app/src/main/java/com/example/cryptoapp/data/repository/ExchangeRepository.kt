package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.model.exchange.Exchange
import retrofit2.Response

class ExchangeRepository(private val manager: NetworkManager) {
    suspend fun getAllExchanges(perPage: Int, page: String): Response<List<Exchange>> {
        return manager.exchangeSource.getExchanges(perPage = perPage, page = page)
    }
}
