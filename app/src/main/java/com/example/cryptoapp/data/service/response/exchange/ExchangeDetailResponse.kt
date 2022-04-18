package com.example.cryptoapp.data.service.response.exchange

import com.google.gson.annotations.SerializedName

data class ExchangeDetailResponse (
    @SerializedName("name")
    val name: String?,
    @SerializedName("year_established")
    val yearEstablished: Long?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("facebook_url")
    val facebookURL: String?,
    @SerializedName("reddit_url")
    val redditURL: String?,
    @SerializedName("other_url_1")
    val otherURL1: String?,
    @SerializedName("other_url_2")
    val otherURL2: String?,
    @SerializedName("centralized")
    val centralized: Boolean?,
    @SerializedName("trust_score")
    val trustScore: Long?,
    @SerializedName("trust_score_rank")
    val trustScoreRank: Long?,
    @SerializedName("trade_volume_24h_btc")
    val tradeVolume24HBtc: Double?,
    @SerializedName("tickers")
    val tickers: List<TickerResponse>?
)

data class TickerResponse (
    @SerializedName("base")
    val base: String?,
    @SerializedName("target")
    val target: String?,
    @SerializedName("volume")
    val volume: Double?,
    @SerializedName("trust_score")
    val trustScore: String?,
    @SerializedName("is_anomaly")
    val isAnomaly: Boolean?,
    @SerializedName("is_stale")
    val isStale: Boolean?,
    @SerializedName("trade_url")
    val tradeURL: String?,
    @SerializedName("coin_id")
    val coinID: String?,
    @SerializedName("target_coin_id")
    val targetCoinID: String?
)

