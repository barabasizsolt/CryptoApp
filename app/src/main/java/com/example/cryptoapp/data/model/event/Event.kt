package com.example.cryptoapp.data.model.event

data class Event(
    val type: String,
    val title: String,
    val description: String,
    val organizer: String,
    val startDate: String,
    val endDate: String,
    val website: String,
    val email: String,
    val venue: String,
    val address: String,
    val city: String,
    val country: String,
    val screenshot: String
)
