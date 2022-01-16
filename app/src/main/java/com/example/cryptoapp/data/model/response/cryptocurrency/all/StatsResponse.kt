package com.example.cryptoapp.data.model.response.cryptocurrency.all

import com.google.gson.annotations.SerializedName

data class StatsResponse(
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
