package com.example.cryptoapp.data.model.cryptoCurrency

import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CoinDetails
import com.google.gson.annotations.SerializedName

data class SingleCryptoData(
    @SerializedName("stats")
    val stats: Stats,
    @SerializedName("coin")
    val coin: CoinDetails
)
