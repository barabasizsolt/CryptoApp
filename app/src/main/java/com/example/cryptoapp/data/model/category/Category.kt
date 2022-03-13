package com.example.cryptoapp.data.model.category

data class Category(
    val id: String,
    val name: String,
    val marketCap: Double,
    val marketCapChange24h: String,
    val content: String,
    val top3Coins: List<String>,
    val volume24h: Double,
    val updatedAt: String,
)