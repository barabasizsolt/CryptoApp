package com.example.cryptoapp.data.shared

import com.example.cryptoapp.data.model.category.Category
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrencyHistory
import com.example.cryptoapp.data.model.exchange.Exchange
import com.example.cryptoapp.data.model.news.News
import com.example.cryptoapp.data.model.response.categories.CategoryResponse
import com.example.cryptoapp.data.model.response.cryptocurrency.all.CryptoCurrencyResponse
import com.example.cryptoapp.data.model.response.cryptocurrency.detail.SingleCryptoCurrencyDetailsResponse
import com.example.cryptoapp.data.model.response.cryptocurrency.history.SingleCryptoCurrencyHistoryResponse
import com.example.cryptoapp.data.model.response.exchange.ExchangeResponse
import com.example.cryptoapp.data.model.response.news.NewsResponse

// CryptoCurrency Mappers
fun CryptoCurrencyResponse.toModel() = when {
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

fun SingleCryptoCurrencyDetailsResponse.toModel() = when {
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

fun SingleCryptoCurrencyHistoryResponse.toModel() = when {
    price == null || timestamp == null -> null
    else -> CryptoCurrencyHistory(
        price = price,
        timestamp = timestamp
    )
}

// Category Mappers
fun CategoryResponse.toModel() = when {
    id == null ||
        name == null ||
        marketCap == null ||
        marketCapChange24h == null ||
        content == null ||
        top3Coins == null ||
        volume24h == null ||
        updatedAt == null -> null
    else -> Category(
        id = id,
        name = name,
        marketCap = marketCap,
        marketCapChange24h = marketCapChange24h.toString(),
        content = content,
        top3Coins = top3Coins,
        volume24h = volume24h,
        updatedAt = updatedAt
    )
}

// Exchange Mappers
fun ExchangeResponse.toModel() = when {
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
fun NewsResponse.toModel() = when {
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
