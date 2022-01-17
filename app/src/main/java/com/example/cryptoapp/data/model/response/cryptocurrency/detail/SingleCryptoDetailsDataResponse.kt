package com.example.cryptoapp.data.model.response.cryptocurrency.detail

import com.example.cryptoapp.data.model.response.cryptocurrency.all.StatsResponse
import com.google.gson.annotations.SerializedName

data class SingleCryptoDetailsDataResponse(
    @SerializedName("stats")
    val stats: StatsResponse,
    @SerializedName("coin")
    val singleCryptoCurrencyDetails: SingleCryptoCurrencyDetailsResponse
)
