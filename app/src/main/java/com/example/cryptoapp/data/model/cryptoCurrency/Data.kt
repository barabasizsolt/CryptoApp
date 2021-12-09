package com.example.cryptoapp.data.model.cryptoCurrency

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("stats")
    val stats: Stats,
    @SerializedName("coins")
    val coins: List<CryptoCurrency>
)
