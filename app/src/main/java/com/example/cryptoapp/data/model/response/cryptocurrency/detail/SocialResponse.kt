package com.example.cryptoapp.data.model.response.cryptocurrency.detail

import com.google.gson.annotations.SerializedName

data class SocialResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("type")
    val type: String,
)
