package com.example.cryptoapp.cache

import com.example.cryptoapp.model.allcryptocurrencies.CryptoCurrency
import com.example.cryptoapp.model.allcryptocurrencies.CryptoCurrencyUIModel
import com.example.cryptoapp.model.cryptocurrencydetail.CoinDetails
import com.example.cryptoapp.model.exchanges.Exchange

object Cache {
    private lateinit var cryptoCurrencies : MutableList<CryptoCurrencyUIModel>
    private lateinit var cryptoCurrency : CoinDetails
    private lateinit var exchanges : MutableList<Exchange>
    private val userWatchLists: MutableList<String> = mutableListOf()

    fun setCryptoCurrencies(data : MutableList<CryptoCurrencyUIModel>) {
        cryptoCurrencies = data
    }

    fun getCryptoCurrencies() = cryptoCurrencies

    fun setCryptoCurrency(data : CoinDetails) {
        cryptoCurrency = data
    }

    fun getCryptoCurrency() = cryptoCurrency

    fun setExchanges(data : MutableList<Exchange>) {
        exchanges = data
    }

    fun getExchanges() = exchanges

    fun addUserWatchList(data: String){
        userWatchLists.add(data)
    }

    fun getUserWatchList() = userWatchLists

    fun deleteUserWatchList() = userWatchLists.clear()
}