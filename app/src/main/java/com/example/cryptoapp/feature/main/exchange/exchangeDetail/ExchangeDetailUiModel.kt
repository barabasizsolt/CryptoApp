package com.example.cryptoapp.feature.main.exchange.exchangeDetail

import com.example.cryptoapp.data.model.exchange.ExchangeDetail
import com.example.cryptoapp.data.model.exchange.Ticker
import com.example.cryptoapp.feature.shared.utils.ChartHistory
import com.example.cryptoapp.feature.shared.utils.convertToPrice
import com.example.cryptoapp.feature.shared.utils.toChartDataSet
import com.github.mikephil.charting.data.LineDataSet

sealed class ExchangeDetailUiModel {

    data class ExchangeDetail(
        val name: String,
        val yearEstablished: String,
        val country: String,
        val url: String,
        val image: String,
        val facebookURL: String,
        val redditURL: String,
        val otherURL1: String,
        val otherURL2: String,
        val centralized: String,
        val trustScore: String,
        val trustScoreRank: String,
        val tradeVolume24HBtc: String,
        val tickers: List<Ticker>
    ) : ExchangeDetailUiModel()

    data class ExchangeDetailHistory(
        val dataSet: LineDataSet
    )

    data class Ticker(
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
}

fun ExchangeDetail.toUiModel() = ExchangeDetailUiModel.ExchangeDetail(
    name = name,
    yearEstablished = yearEstablished.toString(),
    country = country,
    url = url,
    image = image,
    facebookURL = facebookURL,
    redditURL = redditURL,
    otherURL1 = otherURL1,
    otherURL2 = otherURL2,
    centralized = if (centralized) "Centralized" else "Decentralized",
    trustScore = "10/$trustScore",
    trustScoreRank = "Rank #$trustScoreRank",
    tradeVolume24HBtc = tradeVolume24HBtc.toString().convertToPrice().substring(startIndex = 1),
    tickers = tickers.map { ticker -> ticker.toUiModel() }
)

fun Ticker.toUiModel() = ExchangeDetailUiModel.Ticker(
    base = base,
    target = target,
    volume = volume,
    trustScore = trustScore,
    isAnomaly = isAnomaly,
    isStale = isStale,
    tradeURL = tradeURL,
    coinID = coinID,
    targetCoinID = targetCoinID
)

fun List<ChartHistory>.toUiModel(timePeriod: String) = ExchangeDetailUiModel.ExchangeDetailHistory(
    dataSet = this.toChartDataSet(timePeriod = timePeriod)
)
