package com.example.cryptoapp.data.model.cryptoCurrencyDetail

import com.example.cryptoapp.data.model.cryptoCurrency.SingleCryptoData
import com.google.gson.annotations.SerializedName

data class CryptoCurrencyDetails(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: SingleCryptoData
)
