package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CoinDetails
import com.example.cryptoapp.data.model.exchange.Exchange

object Cache {
    private lateinit var cryptoCurrencies: MutableList<CryptoCurrency>
    private lateinit var cryptoCurrency: CoinDetails
    private lateinit var exchanges: MutableList<Exchange>
    private val userWatchLists: MutableList<String> = mutableListOf()

    fun setCryptoCurrencies(data: MutableList<CryptoCurrency>) {
        cryptoCurrencies = data
    }

    fun getCryptoCurrencies() = cryptoCurrencies

    fun setCryptoCurrency(data: CoinDetails) {
        cryptoCurrency = data
    }

    fun getCryptoCurrency() = cryptoCurrency

    fun setExchanges(data: MutableList<Exchange>) {
        exchanges = data
    }

    fun getExchanges() = exchanges

    fun addUserWatchList(data: String) {
        userWatchLists.add(data)
    }

    fun getUserWatchList() = userWatchLists

    fun deleteUserWatchList() = userWatchLists.clear()
}
