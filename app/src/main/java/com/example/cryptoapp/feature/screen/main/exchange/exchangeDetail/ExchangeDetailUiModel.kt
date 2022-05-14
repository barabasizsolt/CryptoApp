package com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail

import com.example.cryptoapp.data.model.exchange.ExchangeDetail
import com.example.cryptoapp.data.model.exchange.Ticker
import com.example.cryptoapp.data.shared.ChartHistory
import com.example.cryptoapp.feature.shared.utils.convertToPrice
import com.example.cryptoapp.feature.shared.utils.getExchangeItemValue
import com.example.cryptoapp.feature.shared.utils.toChartDataSet
import com.github.mikephil.charting.data.LineDataSet

sealed class ExchangeDetailUiModel {

    data class ExchangeDetail(
        val name: String,
        val image: String,
        val centralized: String,
        val trustScore: String,
        val trustScoreRank: String,
        val tradeVolume24HBtc: String,
        val generalDetails: List<GeneralDetailItem>,
        val tickers: List<Ticker>
    ) : ExchangeDetailUiModel()

    data class ExchangeDetailHistory(
        val dataSet: LineDataSet
    )
}

data class GeneralDetailItem(
    val title: String,
    val value: String
)

fun ExchangeDetail.toUiModel() = ExchangeDetailUiModel.ExchangeDetail(
    name = name,
    image = image,
    generalDetails = buildList {
        add(GeneralDetailItem(title = "Year established", value = yearEstablished.toString().getExchangeItemValue()))
        add(GeneralDetailItem(title = "Country", value = country))
        add(GeneralDetailItem(title = "Homepage", value = url.getExchangeItemValue()))
        add(GeneralDetailItem(title = "Facebook", value = facebookURL.getExchangeItemValue()))
        add(GeneralDetailItem(title = "Reddit", value = redditURL.getExchangeItemValue()))
        add(GeneralDetailItem(title = "Other URL", value = otherURL1.getExchangeItemValue()))
        add(GeneralDetailItem(title = "Other URL", value = otherURL2.getExchangeItemValue()))
    },
    centralized = if (centralized) "Centralized" else "Decentralized",
    trustScore = "10/$trustScore",
    trustScoreRank = "Rank #$trustScoreRank",
    tradeVolume24HBtc = tradeVolume24HBtc.toString().convertToPrice().substring(startIndex = 1),
    tickers = tickers.map { ticker -> ticker.toUiModel() }
)

fun Ticker.toUiModel() = Ticker(
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
