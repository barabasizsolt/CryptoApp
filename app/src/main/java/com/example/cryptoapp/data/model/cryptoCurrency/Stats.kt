package com.example.cryptoapp.data.model.cryptoCurrency

data class Stats(
    val total: Long,
    val totalCoins: Long,
    val totalMarkets: Long,
    val totalExchanges: Long,
    val totalMarketCap: String,
    val total24hVolume: String
)
