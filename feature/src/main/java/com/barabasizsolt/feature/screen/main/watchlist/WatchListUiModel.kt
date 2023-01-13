package com.barabasizsolt.feature.screen.main.watchlist

import com.barabasizsolt.feature.shared.utils.convertToCompactPrice
import com.barabasizsolt.feature.shared.utils.formatInput
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency


data class WatchListSummaryUiModel(
    val totalPrice: String,
    val mvpCoinUrl: String,
    val totalCoin: String,
    val totalChange: String,
    val totalVolume: String,
    val totalMarketCap: String
)

fun List<CryptoCurrency>.toWatchListSummaryUiModel() = WatchListSummaryUiModel(
    totalPrice = sumOf { it.price.toDouble() }.toString().convertToCompactPrice(),
    mvpCoinUrl = sortedWith(comparator = compareBy { it.price.toDouble() })[lastIndex].iconUrl,
    totalCoin = size.toString(),
    totalChange = sumOf { it.change.toDouble() }.toString().formatInput(),
    totalVolume = sumOf { it.volume.toDouble() }.toString().convertToCompactPrice(),
    totalMarketCap = sumOf { it.marketCap.toDouble() }.toString().convertToCompactPrice()
)