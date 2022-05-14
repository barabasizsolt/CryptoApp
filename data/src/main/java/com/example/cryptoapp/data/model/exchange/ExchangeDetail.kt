package com.example.cryptoapp.data.model.exchange

data class ExchangeDetail (
    val name: String,
    val yearEstablished: Long,
    val country: String,
    val url: String,
    val image: String,
    val facebookURL: String,
    val redditURL: String,
    val otherURL1: String,
    val otherURL2: String,
    val centralized: Boolean,
    val trustScore: Long,
    val trustScoreRank: Long,
    val tradeVolume24HBtc: Double,
    val tickers: List<Ticker>
)

data class Ticker (
    val base: String,
    val target: String,
    val volume: Double,
    val trustScore: String,
    val isAnomaly: Boolean,
    val isStale: Boolean,
    val tradeURL: String,
    val coinID: String,
    val targetCoinID: String
)