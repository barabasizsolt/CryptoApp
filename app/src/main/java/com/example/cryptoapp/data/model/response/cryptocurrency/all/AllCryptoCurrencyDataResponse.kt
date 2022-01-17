package com.example.cryptoapp.data.model.response.cryptocurrency.all

import com.google.gson.annotations.SerializedName

data class AllCryptoCurrencyDataResponse(
    @SerializedName("stats")
    val stats: StatsResponse,
    @SerializedName("coins")
    val coins: List<CryptoCurrencyResponse>
)
