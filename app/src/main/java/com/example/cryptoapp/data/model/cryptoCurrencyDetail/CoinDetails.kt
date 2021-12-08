package com.example.cryptoapp.data.model.cryptoCurrencyDetail

import com.example.cryptoapp.data.model.cryptoCurrency.AllTimeHigh
import com.example.cryptoapp.data.model.cryptoCurrency.Social
import com.example.cryptoapp.data.model.cryptoCurrency.Supply
import com.google.gson.annotations.SerializedName

data class CoinDetails(
    val uuid: String?,
    val symbol: String?,
    val name: String?,
    val description: String?,
    val iconUrl: String?,
    val websiteUrl: String?,
    val links: List<Social>?,
    val supply: Supply,
    val numberOfMarkets: Long?,
    val numberOfExchanges: Long?,
    @SerializedName("24hVolume")
    val volume: String?,
    val marketCap: String?,
    val price: String?,
    val btcPrice: String?,
    val change: String?,
    val rank: Long?,
    val sparkline: List<String>?,
    val allTimeHigh: AllTimeHigh,
    @SerializedName("coinrankingUrl")
    val coinRankingUrl: String?,
    val tier: Long?,
    val lowVolume: Boolean?
)
