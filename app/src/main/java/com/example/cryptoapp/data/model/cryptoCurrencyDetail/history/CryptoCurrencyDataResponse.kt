package com.example.cryptoapp.data.model.cryptoCurrencyDetail.history

import com.google.gson.annotations.SerializedName

data class CryptoCurrencyDataResponse(
    @SerializedName("change")
    val change: String,
    @SerializedName("history")
    val history: List<SingleCryptoCurrencyHistoryResponse>
)
