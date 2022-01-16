package com.example.cryptoapp.data.model.response.cryptocurrency.history

import com.google.gson.annotations.SerializedName

data class CryptoCurrencyHistoryDataResponse(
    @SerializedName("change")
    val change: String,
    @SerializedName("history")
    val history: List<SingleCryptoCurrencyHistoryResponse>
)
