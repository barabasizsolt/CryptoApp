package com.example.cryptoapp.data.service.response.cryptocurrency.detail

import com.google.gson.annotations.SerializedName

data class CryptoCurrencyDetailsResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: SingleCryptoDetailsDataResponse
)
