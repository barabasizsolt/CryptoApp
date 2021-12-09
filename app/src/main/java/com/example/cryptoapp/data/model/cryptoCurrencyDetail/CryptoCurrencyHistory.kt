package com.example.cryptoapp.data.model.cryptoCurrencyDetail

import com.google.gson.annotations.SerializedName

data class CryptoCurrencyHistory(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: CryptoData
)
