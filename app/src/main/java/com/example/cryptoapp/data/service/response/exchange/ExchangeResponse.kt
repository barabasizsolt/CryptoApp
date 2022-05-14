package com.example.cryptoapp.data.service.response.exchange

import com.google.gson.annotations.SerializedName

data class ExchangeResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("year_established")
    val yearEstablished: Int?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("has_trading_incentive")
    val hasTradingIncentive: Boolean?,
    @SerializedName("trust_score")
    val trustScore: Double?,
    @SerializedName("trust_score_rank")
    val trustScoreRank: Long?,
    @SerializedName("trade_volume_24h_btc")
    val tradeVolume24HBtc: Double?,
    @SerializedName("trade_volume_24h_btc_normalized")
    val tradeVolume24HBtcNormalized: Double?
)
