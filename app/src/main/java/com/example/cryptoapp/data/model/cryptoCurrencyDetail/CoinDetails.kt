package com.example.cryptoapp.data.model.cryptoCurrencyDetail

import com.example.cryptoapp.data.model.cryptoCurrency.AllTimeHigh
import com.example.cryptoapp.data.model.cryptoCurrency.Social
import com.example.cryptoapp.data.model.cryptoCurrency.Supply
import com.google.gson.annotations.SerializedName

data class CoinDetails(
    @SerializedName("uuid")
    val uuid: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("iconUrl")
    val iconUrl: String?,
    @SerializedName("websiteUrl")
    val websiteUrl: String?,
    @SerializedName("links")
    val links: List<Social>?,
    @SerializedName("supply")
    val supply: Supply,
    @SerializedName("numberOfMarkets")
    val numberOfMarkets: Long?,
    @SerializedName("numberOfExchanges")
    val numberOfExchanges: Long?,
    @SerializedName("24hVolume")
    val volume: String?,
    @SerializedName("marketCap")
    val marketCap: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("btcPrice")
    val btcPrice: String?,
    @SerializedName("change")
    val change: String?,
    @SerializedName("rank")
    val rank: Long?,
    @SerializedName("sparkline")
    val sparkline: List<String>?,
    @SerializedName("allTimeHigh")
    val allTimeHigh: AllTimeHigh,
    @SerializedName("coinrankingUrl")
    val coinRankingUrl: String?,
    @SerializedName("tier")
    val tier: Long?,
    @SerializedName("lowVolume")
    val lowVolume: Boolean?
)
