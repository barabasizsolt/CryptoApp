package com.example.cryptoapp.data.model.event

import com.google.gson.annotations.SerializedName

data class AllEvents(
    @SerializedName("data")
    val data: List<Event>,
    @SerializedName("count")
    val count: Long = 0,
    @SerializedName("page")
    val page: Long = 0
)
