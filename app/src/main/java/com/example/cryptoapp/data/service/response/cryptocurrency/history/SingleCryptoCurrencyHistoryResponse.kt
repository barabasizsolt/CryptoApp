package com.example.cryptoapp.data.service.response.cryptocurrency.history

import com.google.gson.annotations.SerializedName

data class SingleCryptoCurrencyHistoryResponse(
    @SerializedName("price")
    val price: String?,
    @SerializedName("timestamp")
    val timestamp: Long?
)
