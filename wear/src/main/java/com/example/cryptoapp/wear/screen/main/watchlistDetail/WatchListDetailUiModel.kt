package com.example.cryptoapp.wear.screen.main.watchlistDetail

import android.os.Parcelable
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrencyDetails
import com.example.cryptoapp.wear.common.convertToCompactPrice
import com.example.cryptoapp.wear.common.convertToPrice
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CryptoCurrencyDetailUiModel(
    val uuid: String,
    val symbol: String,
    val name: String,
    val price: String,
    val iconUrl: String,
    val change: String,
    val volume: String,
    val marketCap: String
) : Parcelable

fun CryptoCurrencyDetails.toUiModel() = CryptoCurrencyDetailUiModel(
    uuid = uuid,
    symbol = symbol,
    name = name,
    price = price.convertToPrice(),
    iconUrl = iconUrl,
    change = change,
    volume = volume.convertToCompactPrice(),
    marketCap = marketCap.convertToCompactPrice()
)