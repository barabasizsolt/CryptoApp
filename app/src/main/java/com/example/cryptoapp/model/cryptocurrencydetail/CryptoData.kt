package com.example.cryptoapp.model.cryptocurrencydetail

data class CryptoData (
    val change : String,
    val history : List<CryptoHistory>
    )