package com.example.cryptoapp.wear.screen.cryptocurrency

import android.os.Parcelable
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CryptoCurrencyUiModel(
    val uuid: String,
    val symbol: String,
    val name: String,
    val iconUrl: String,
    val marketCap: String,
    val price: String,
    val change: String,
    val volume: String,
) : Parcelable

fun CryptoCurrency.toUiModel() = CryptoCurrencyUiModel(
    uuid = uuid,
    symbol = symbol,
    name = name,
    iconUrl = iconUrl,
    marketCap = marketCap,
    price = price,
    change = change,
    volume = volume
)