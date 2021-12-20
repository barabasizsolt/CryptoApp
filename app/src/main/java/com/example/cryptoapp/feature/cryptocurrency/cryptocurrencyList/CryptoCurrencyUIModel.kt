package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency
import com.example.cryptoapp.feature.shared.convertToCompactPrice
import com.example.cryptoapp.feature.shared.convertToPrice

data class CryptoCurrencyUIModel(
    val cryptoCurrency: CryptoCurrency,
    val timePeriod: String
) {
    val formattedPrice = cryptoCurrency.price.convertToPrice()
    val formattedVolume = cryptoCurrency.volume.convertToCompactPrice()
    val formattedMarketCap = cryptoCurrency.marketCap.convertToCompactPrice()
}
