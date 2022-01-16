package com.example.cryptoapp.data.model.response.cryptocurrency.detail

import com.google.gson.annotations.SerializedName

data class AllTimeHighResponse(
    @SerializedName("price")
    val price: String,
    @SerializedName("timestamp")
    val timestamp: Long
)
