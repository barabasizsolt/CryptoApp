package com.example.cryptoapp.data.model.news

data class News(
    val title: String,
    val description: String,
    val url: String,
    val updated: Long,
    val site: String,
    val logo: String
)
