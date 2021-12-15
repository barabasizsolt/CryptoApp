package com.example.cryptoapp.data.model.cryptoCurrencyDetail.details

import com.example.cryptoapp.data.model.cryptoCurrency.AllTimeHighResponse

data class CryptoCurrencyDetails(
    val uuid: String?,
    val symbol: String?,
    val name: String?,
    val description: String?,
    val iconUrl: String?,
    val totalSupply: String?,
    val circulating: String?,
    val volume: String?,
    val marketCap: String?,
    val price: String?,
    val btcPrice: String?,
    val change: String?,
    val rank: Long?,
    val allTimeHigh: AllTimeHighResponse
)
