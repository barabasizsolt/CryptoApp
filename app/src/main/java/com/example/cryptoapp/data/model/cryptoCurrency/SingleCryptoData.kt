package com.example.cryptoapp.data.model.cryptoCurrency

import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CoinDetails

data class SingleCryptoData (
    val stats: Stats,
    val coin : CoinDetails
)