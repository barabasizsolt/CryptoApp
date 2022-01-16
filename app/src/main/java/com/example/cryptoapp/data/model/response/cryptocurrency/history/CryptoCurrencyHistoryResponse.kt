package com.example.cryptoapp.data.model.response.cryptocurrency.history

import com.google.gson.annotations.SerializedName

data class CryptoCurrencyHistoryResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: CryptoCurrencyHistoryDataResponse
)
