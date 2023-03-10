package com.example.cryptoapp.data.service.response.cryptocurrency.detail

import com.google.gson.annotations.SerializedName

data class SupplyResponse(
    @SerializedName("confirmed")
    val confirmed: Boolean?,
    @SerializedName("total")
    val total: String?,
    @SerializedName("circulating")
    val circulating: String?
)
