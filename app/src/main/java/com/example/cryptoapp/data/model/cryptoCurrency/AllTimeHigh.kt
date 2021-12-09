package com.example.cryptoapp.data.model.cryptoCurrency

import com.google.gson.annotations.SerializedName

data class AllTimeHigh(
    @SerializedName("price")
    val price: String,
    @SerializedName("timestamp")
    val timestamp: Long
)
