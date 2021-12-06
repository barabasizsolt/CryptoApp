package com.example.cryptoapp.model.allcryptocurrencies

import com.google.gson.annotations.SerializedName

data class CryptoCurrency (
    val uuid : String?,
    val symbol : String?,
    val name : String?,
    val color : String?,
    val iconUrl : String?,
    val marketCap : String?,
    val price : String?,
    val listedAt : Long?,
    val tier : Long?,
    val change : String?,
    val rank : Long?,
    val sparkline : List<String>?,
    val lowVolume : Boolean?,
    @SerializedName("coinrankingUrl")
    val coinRankingUrl : String?,
    @SerializedName("24hVolume")
    val volume : String?,
    val btcPrice : String?
)