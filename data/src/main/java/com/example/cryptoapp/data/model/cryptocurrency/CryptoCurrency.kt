package com.example.cryptoapp.data.model.cryptocurrency

data class CryptoCurrency(
    val uuid: String,
    val symbol: String,
    val name: String,
    val iconUrl: String,
    val marketCap: String,
    val price: String,
    val change: String,
    val volume: String,
)
