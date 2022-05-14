package com.example.cryptoapp.data.service.response.cryptocurrency.all

import com.google.gson.annotations.SerializedName

data class AllCryptoCurrencyDataResponse(
    @SerializedName("stats")
    val stats: StatsResponse,
    @SerializedName("coins")
    val coins: List<CryptoCurrencyResponse>
)
