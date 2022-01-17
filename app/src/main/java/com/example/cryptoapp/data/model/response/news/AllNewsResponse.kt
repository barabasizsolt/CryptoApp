package com.example.cryptoapp.data.model.response.news

import com.google.gson.annotations.SerializedName

data class AllNewsResponse(
    @SerializedName("data")
    val data: List<NewsResponse>,
    @SerializedName("count")
    val count: Long = 0,
    @SerializedName("page")
    val page: Long = 0
)
