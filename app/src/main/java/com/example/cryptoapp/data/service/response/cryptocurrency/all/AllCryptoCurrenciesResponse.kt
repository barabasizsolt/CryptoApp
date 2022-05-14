package com.example.cryptoapp.data.service.response.cryptocurrency.all

import com.google.gson.annotations.SerializedName

data class AllCryptoCurrenciesResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: AllCryptoCurrencyDataResponse,
)
