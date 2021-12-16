package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import com.example.cryptoapp.data.constant.CryptoConstant
import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency

data class CryptoCurrencyUIModel(
    val cryptoCurrency: CryptoCurrency,
    val timePeriod: String
) {
    val formattedPrice = CryptoConstant.convertToPrice(cryptoCurrency.price)
    val formattedVolume = CryptoConstant.convertToCompactPrice(cryptoCurrency.volume)
    val formattedMarketCap = CryptoConstant.convertToCompactPrice(cryptoCurrency.marketCap)
}
