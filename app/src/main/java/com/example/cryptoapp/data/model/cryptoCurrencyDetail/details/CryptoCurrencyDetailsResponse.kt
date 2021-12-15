package com.example.cryptoapp.data.model.cryptoCurrencyDetail.details

import com.example.cryptoapp.data.model.cryptoCurrency.SingleCryptoDataResponse
import com.google.gson.annotations.SerializedName

data class CryptoCurrencyDetailsResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: SingleCryptoDataResponse
)
