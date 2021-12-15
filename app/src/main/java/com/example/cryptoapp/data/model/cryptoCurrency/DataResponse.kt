package com.example.cryptoapp.data.model.cryptoCurrency

import com.google.gson.annotations.SerializedName

data class DataResponse(
    @SerializedName("stats")
    val stats: StatsResponse,
    @SerializedName("coins")
    val coins: List<CryptoCurrencyResponse>
)
