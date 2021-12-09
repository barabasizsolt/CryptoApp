package com.example.cryptoapp.data.model.cryptoCurrency

import com.google.gson.annotations.SerializedName

data class Supply(
    @SerializedName("confirmed")
    val confirmed: Boolean?,
    @SerializedName("total")
    val total: String?,
    @SerializedName("circulating")
    val circulating: String?
)
