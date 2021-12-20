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

fun CryptoCurrencyResponse.toCryptoCurrency() = when {
    symbol == null ||
        name == null ||
        iconUrl == null ||
        marketCap == null ||
        price == null ||
        change == null ||
        volume == null -> null
    else -> CryptoCurrency(
        uuid = uuid,
        symbol = symbol,
        name = name,
        iconUrl = iconUrl,
        marketCap = marketCap,
        price = price,
        change = change,
        volume = volume,
    )
}

fun CoinDetailsResponse.toCryptoCurrencyDetails() = when {
    uuid == null ||
        symbol == null ||
        name == null ||
        iconUrl == null ||
        marketCap == null ||
        price == null ||
        change == null ||
        rank == null ||
        supply.total == null ||
        supply.circulating == null ||
        btcPrice == null ||
        description == null ||
        volume == null -> null
    else -> CryptoCurrencyDetails(
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
}

fun CryptoCurrencyHistoryResponse.toCryptoCurrencyHistory() = CryptoCurrencyHistory(
    history = data.history.filter { response -> !response.price.isNullOrEmpty() }
)

// Exchange Mappers
fun ExchangeResponse.toExchange() = when {
    id == null ||
        name == null ||
        yearEstablished == null ||
        country == null ||
        description == null ||
        url == null ||
        image == null ||
        trustScore == null ||
        tradeVolume24HBtc == null -> null
    else -> Exchange(
        id = id,
        name = name,
        yearEstablished = yearEstablished,
        country = country,
        description = description,
        url = url,
        image = image,
        trustScore = trustScore,
        volume = tradeVolume24HBtc
    )
}

// Event Mappers
fun EventResponse.toEvent() = when {
    type == null ||
        title == null ||
        description == null ||
        organizer == null ||
        startDate == null ||
        endDate == null ||
        website == null ||
        email == null ||
        venue == null ||
        address == null ||
        city == null ||
        country == null ||
        screenshot == null -> null
    else -> Event(
        type = type,
        title = title,
        description = description,
        organizer = organizer,
        startDate = startDate,
        endDate = endDate,
        website = website,
        email = email,
        venue = venue,
        address = address,
        city = city,
        country = country,
        screenshot = screenshot
    )
}
