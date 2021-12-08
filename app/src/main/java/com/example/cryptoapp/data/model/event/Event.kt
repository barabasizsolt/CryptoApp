package com.example.cryptoapp.data.model.event

import com.google.gson.annotations.SerializedName

data class Event(
    val type: String?,
    val title: String?,
    val description: String?,
    val organizer: String?,
    @SerializedName("start_date")
    val startDate: String?,
    @SerializedName("end_date")
    val endDate: String?,
    val website: String?,
    val email: String?,
    val venue: String?,
    val address: String?,
    val city: String?,
    val country: String?,
    val screenshot: String?
)
