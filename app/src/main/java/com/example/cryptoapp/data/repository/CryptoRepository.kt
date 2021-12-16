package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.shared.toCryptoCurrency
import com.example.cryptoapp.data.shared.toCryptoCurrencyDetails
import com.example.cryptoapp.data.shared.toCryptoCurrencyHistory
import java.lang.IllegalStateException

class CryptoRepository(private val manager: NetworkManager) {
    suspend fun getAllCryptoCurrencies(
        orderBy: String,
        orderDirection: String,
        offset: Int,
        tags: Set<String>,
        timePeriod: String
    ) = manager.cryptoSource.getAllCryptoCurrencies(
        orderBy = orderBy,
        orderDirection = orderDirection,
        offset = offset,
        tags = tags,
        timePeriod = timePeriod
    ).body()?.data?.coins?.map {
        currencyResponse ->
        currencyResponse.toCryptoCurrency()
    } ?: throw IllegalStateException("Invalid data returned by the server")

    suspend fun getCryptoCurrencyDetails(
        uuid: String
    ) = manager.cryptoSource.getCryptoCurrencyDetails(
        uuid = uuid
    ).body()?.data?.coin?.toCryptoCurrencyDetails() ?: throw IllegalStateException("Invalid data returned by the server")

    suspend fun getCryptoCurrencyHistory(
        uuid: String,
        timePeriod: String
    ) = manager.cryptoSource.getCryptoCurrencyHistory(
        uuid = uuid,
        timePeriod = timePeriod
    ).body()?.toCryptoCurrencyHistory() ?: throw IllegalStateException("Invalid data returned by the server")
}
