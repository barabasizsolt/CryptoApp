package com.example.cryptoapp.wear.screen.cryptocurrency.watchlist

import android.os.Parcelable
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency
import com.example.cryptoapp.wear.common.convertToCompactPrice
import com.example.cryptoapp.wear.common.formatInput
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WatchListSummaryUiModel(
    val totalPrice: String,
    val mvpCoinUrl: String,
    val totalCoin: String,
    val totalChange: String,
    val totalVolume: String,
    val totalMarketCap: String
) : Parcelable

fun List<CryptoCurrency>.toWatchListSummaryUiModel() = WatchListSummaryUiModel(
    totalPrice = sumOf { it.price.toDouble() }.toString().convertToCompactPrice(),
    mvpCoinUrl = sortedWith(comparator = compareBy { it.price.toDouble() })[lastIndex].iconUrl,
    totalCoin = size.toString(),
    totalChange = sumOf { it.change.toDouble() }.toString().formatInput(),
    totalVolume = sumOf { it.volume.toDouble() }.toString().convertToCompactPrice(),
    totalMarketCap = sumOf { it.marketCap.toDouble() }.toString().convertToCompactPrice()
)