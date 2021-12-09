package com.example.cryptoapp.data.model.cryptoCurrency

import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("total")
    val total: Long,
    @SerializedName("totalCoins")
    val totalCoins: Long,
    @SerializedName("totalMarkets")
    val totalMarkets: Long,
    @SerializedName("totalExchanges")
    val totalExchanges: Long,
    @SerializedName("totalMarketCap")
    val totalMarketCap: String,
    @SerializedName("total24hVolume")
    val total24hVolume: String
)
