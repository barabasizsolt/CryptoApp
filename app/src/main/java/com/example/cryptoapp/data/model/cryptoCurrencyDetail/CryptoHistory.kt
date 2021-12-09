package com.example.cryptoapp.data.model.cryptoCurrencyDetail

import com.google.gson.annotations.SerializedName

data class CryptoHistory(
    @SerializedName("price")
    val price: String?,
    @SerializedName("timestamp")
    val timestamp: Long
)
