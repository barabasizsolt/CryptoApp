package com.example.cryptoapp.data.model.event

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("type")
    val type: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("organizer")
    val organizer: String?,
    @SerializedName("start_date")
    val startDate: String?,
    @SerializedName("end_date")
    val endDate: String?,
    @SerializedName("website")
    val website: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("venue")
    val venue: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("screenshot")
    val screenshot: String?
)
