package com.example.cryptoapp.data.constant

import com.example.cryptoapp.data.model.exchange.Exchange
import com.example.cryptoapp.data.model.exchange.ExchangeResponse
import com.example.cryptoapp.data.model.exchange.ExchangeUIModel

object ExchangeConstant {
    const val PER_PAGE: Int = 50
    const val PAGE: String = "1"

    fun ExchangeResponse.toExchange() = Exchange(
        exchange = this
    )

    fun Exchange.toExchangeUIModel() = ExchangeUIModel(
        id = exchange.id.toString(),
        name = exchange.name.toString(),
        logo = exchange.image.toString(),
        trustScore = exchange.trustScore.toString(),
        volume = exchange.tradeVolume24HBtc.toString()
    )
}
