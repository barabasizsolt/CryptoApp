package com.example.cryptoapp.data.model.cryptoCurrencyDetail

import com.google.gson.annotations.SerializedName

data class CryptoData(
    @SerializedName("change")
    val change: String,
    @SerializedName("history")
    val history: List<CryptoHistory>
)
