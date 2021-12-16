package com.example.cryptoapp.feature.exchange

import com.example.cryptoapp.feature.shared.convertToCompactPrice

data class ExchangeUIModel(
    val id: String,
    val name: String,
    val logo: String,
    val trustScore: String,
    val volume: String
) {
    val formattedVolume = volume.convertToCompactPrice()
}
