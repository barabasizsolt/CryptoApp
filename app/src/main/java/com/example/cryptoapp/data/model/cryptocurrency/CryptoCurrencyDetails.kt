package com.example.cryptoapp.data.model.cryptocurrency

import com.example.cryptoapp.data.model.response.cryptocurrency.detail.AllTimeHighResponse

data class CryptoCurrencyDetails(
    val uuid: String,
    val symbol: String,
    val name: String,
    val description: String,
    val iconUrl: String,
    val totalSupply: String,
    val circulating: String,
    val volume: String,
    val marketCap: String,
    val price: String,
    val btcPrice: String,
    val change: String,
    val rank: Int,
    val allTimeHigh: AllTimeHighResponse
)
