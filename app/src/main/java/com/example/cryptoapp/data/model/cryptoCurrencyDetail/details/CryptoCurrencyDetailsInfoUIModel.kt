package com.example.cryptoapp.data.model.cryptoCurrencyDetail.details

import com.example.cryptoapp.data.model.cryptoCurrency.AllTimeHighResponse

data class CryptoCurrencyDetailsInfoUIModel (
    val description: String,
    val circulating: String,
    val btcPrice: String,
    val rank: String,
    val totalSupply: String,
    val allTimeHigh: AllTimeHighResponse
    )