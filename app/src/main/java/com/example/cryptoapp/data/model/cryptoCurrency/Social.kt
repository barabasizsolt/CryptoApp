package com.example.cryptoapp.data.model.cryptoCurrency

import com.google.gson.annotations.SerializedName

data class Social(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("type")
    val type: String,
)
