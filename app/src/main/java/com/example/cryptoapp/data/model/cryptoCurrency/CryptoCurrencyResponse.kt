package com.example.cryptoapp.data.model.cryptoCurrency

import com.google.gson.annotations.SerializedName

data class CryptoCurrencyResponse(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("iconUrl")
    val iconUrl: String?,
    @SerializedName("marketCap")
    val marketCap: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("listedAt")
    val listedAt: Long?,
    @SerializedName("tier")
    val tier: Long?,
    @SerializedName("change")
    val change: String?,
    @SerializedName("rank")
    val rank: Long?,
    @SerializedName("sparkline")
    val sparkline: List<String>?,
    @SerializedName("lowVolume")
    val lowVolume: Boolean?,
    @SerializedName("coinrankingUrl")
    val coinRankingUrl: String?,
    @SerializedName("24hVolume")
    val volume: String?,
    @SerializedName("btcPrice")
    val btcPrice: String?
)
