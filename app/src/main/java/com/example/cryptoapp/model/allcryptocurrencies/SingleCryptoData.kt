package com.example.cryptoapp.model.allcryptocurrencies

import com.example.cryptoapp.model.cryptocurrencydetail.CoinDetails

data class SingleCryptoData (
    val stats: Stats,
    val coin : CoinDetails
)