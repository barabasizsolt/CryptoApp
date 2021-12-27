package com.example.cryptoapp.data.shared

import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency
import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrencyResponse
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CoinDetailsResponse
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.CryptoHistoryItem
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.SingleCryptoCurrencyHistoryResponse
import com.example.cryptoapp.data.model.exchange.Exchange
import com.example.cryptoapp.data.model.exchange.ExchangeResponse
import com.example.cryptoapp.data.model.news.News
import com.example.cryptoapp.data.model.news.NewsResponse

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

fun SingleCryptoCurrencyHistoryResponse.toCryptoHistoryItem() = when {
    price == null || timestamp == null -> null
    else -> CryptoHistoryItem(
        price = price,
        timestamp = timestamp
    )
}

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

// News Mappers
fun NewsResponse.toNews() = when {
    title == null ||
        description == null ||
        url == null ||
        updated == null ||
        site == null ||
        logo == null -> null
    else -> News(
        title = title,
        description = description,
        url = url,
        updated = updated,
        site = site,
        logo = logo
    )
}
