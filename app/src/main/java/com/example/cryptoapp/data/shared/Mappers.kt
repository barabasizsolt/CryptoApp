package com.example.cryptoapp.data.shared

import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency
import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrencyResponse
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CoinDetailsResponse
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.CryptoCurrencyHistory
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.CryptoCurrencyHistoryResponse
import com.example.cryptoapp.data.model.event.Event
import com.example.cryptoapp.data.model.event.EventResponse
import com.example.cryptoapp.data.model.exchange.Exchange
import com.example.cryptoapp.data.model.exchange.ExchangeResponse

// CryptoCurrency Mappers
fun CryptoCurrencyResponse.toCryptoCurrency() = CryptoCurrency(
    uuid = uuid,
    symbol = symbol.toString(),
    name = name.toString(),
    iconUrl = iconUrl.toString(),
    marketCap = marketCap.toString(),
    price = price.toString(),
    change = change.toString(),
    volume = volume.toString(),
)

fun CoinDetailsResponse.toCryptoCurrencyDetails() = CryptoCurrencyDetails(
    uuid = uuid,
    symbol = symbol,
    name = name,
    iconUrl = iconUrl,
    marketCap = marketCap,
    price = price,
    change = change,
    volume = volume,
    rank = rank,
    totalSupply = supply.total,
    circulating = supply.circulating,
    btcPrice = btcPrice,
    allTimeHigh = allTimeHigh,
    description = description
)

fun CryptoCurrencyHistoryResponse.toCryptoCurrencyHistory() = CryptoCurrencyHistory(
    history = data.history
)

// Exchange Mappers
fun ExchangeResponse.toExchange() = Exchange(
    exchange = this
)

// Event Mappers
fun EventResponse.toEvent() = Event(
    event = this
)
