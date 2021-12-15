package com.example.cryptoapp.data.model.cryptoCurrency

import com.google.gson.annotations.SerializedName

data class AllCryptoCurrenciesResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: DataResponse,
)
