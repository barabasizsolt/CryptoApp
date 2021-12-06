package com.example.cryptoapp.model.cryptocurrencydetail

import com.example.cryptoapp.model.allcryptocurrencies.AllTimeHigh
import com.example.cryptoapp.model.allcryptocurrencies.Social
import com.example.cryptoapp.model.allcryptocurrencies.Supply
import com.google.gson.annotations.SerializedName

data class CoinDetails (
    val uuid : String?,
    val symbol : String?,
    val name : String?,
    val description : String?,
    val iconUrl : String?,
    val websiteUrl : String?,
    val links : List<Social>?,
    val supply : Supply,
    val numberOfMarkets : Long?,
    val numberOfExchanges : Long?,
    @SerializedName("24hVolume")
    val volume : String?,
    val marketCap : String?,
    val price : String?,
    val btcPrice : String?,
    val change : String?,
    val rank : Long?,
    val sparkline : List<String>?,
    val allTimeHigh : AllTimeHigh,
    @SerializedName("coinrankingUrl")
    val coinRankingUrl : String?,
    val tier : Long?,
    val lowVolume : Boolean?
    )