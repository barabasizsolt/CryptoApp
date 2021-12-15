package com.example.cryptoapp.feature.cryptocurrency.cryptocurrencyDetails

import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.SingleCryptoCurrencyHistoryResponse

data class CryptoCurrencyHistoryUIModel(
    val history: MutableList<SingleCryptoCurrencyHistoryResponse>
)
