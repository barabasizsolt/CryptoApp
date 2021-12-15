package com.example.cryptoapp.data.model.cryptoCurrency

import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CoinDetailsResponse
import com.google.gson.annotations.SerializedName

data class SingleCryptoDataResponse(
    @SerializedName("stats")
    val stats: StatsResponse,
    @SerializedName("coin")
    val coin: CoinDetailsResponse
)
