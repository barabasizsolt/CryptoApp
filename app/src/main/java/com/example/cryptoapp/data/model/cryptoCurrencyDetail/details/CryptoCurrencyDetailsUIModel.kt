package com.example.cryptoapp.data.model.cryptoCurrencyDetail.details

import com.example.cryptoapp.data.model.cryptoCurrency.AllTimeHighResponse

data class CryptoCurrencyDetailsUIModel (
    val uuid: String,
    val symbol: String,
    val name: String,
    val iconUrl: String,
    val volume: String,
    val marketCap: String,
    val price: String,
    val change: String
    )
