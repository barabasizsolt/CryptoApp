package com.example.cryptoapp.data.model.cryptoCurrency

import com.google.gson.annotations.SerializedName

data class AllCryptoCurrencies(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Data,
)
