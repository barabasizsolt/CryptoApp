package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyList

import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency

data class CryptoCurrencyUIModel(
    val cryptoCurrency: CryptoCurrency,
    val timePeriod: String
)
