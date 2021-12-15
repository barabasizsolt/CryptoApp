package com.example.cryptoapp.data.model.cryptoCurrency

import com.google.gson.annotations.SerializedName

data class AllTimeHighResponse(
    @SerializedName("price")
    val price: String,
    @SerializedName("timestamp")
    val timestamp: Long
)
