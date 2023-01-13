package com.example.cryptoapp.data.model.exchange

data class Exchange(
    val id: String,
    val name: String,
    val yearEstablished: Int,
    val country: String,
    val description: String,
    val url: String,
    val image: String,
    val trustScore: Double,
    val volume: Double,
    val rank: Long
)
