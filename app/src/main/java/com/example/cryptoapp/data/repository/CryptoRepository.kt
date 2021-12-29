package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.details.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.history.CryptoHistoryItem
import com.example.cryptoapp.data.shared.toCryptoCurrency
import com.example.cryptoapp.data.shared.toCryptoCurrencyDetails
import com.example.cryptoapp.data.shared.toCryptoHistoryItem
import java.lang.IllegalStateException

class CryptoRepository(private val manager: NetworkManager) {

    companion object {
        const val LIMIT = 50
    }

    private var cryptoCurrenciesCache: MutableList<CryptoCurrency>? = null
    private var cryptoCurrencyDetailsCache: CryptoCurrencyDetails? = null
    private var cryptoCurrencyHistoryCache: MutableList<CryptoHistoryItem>? = null
    private var lastCryptoCurrencyOffset = 0

    suspend fun getAllCryptoCurrencies(
        orderBy: String,
        orderDirection: String,
        tags: List<String>,
        timePeriod: String,
        refreshType: RefreshType
    ): List<CryptoCurrency> = when (refreshType) {
        RefreshType.FORCE_REFRESH -> loadAllCryptoCurrencies(
            orderBy = orderBy,
            orderDirection = orderDirection,
            offset = 0,
            tags = tags,
            timePeriod = timePeriod
        ).let { newData ->
            newData.also { cryptoCurrenciesCache = it.toMutableList() }
        }
        RefreshType.CACHE_IF_POSSIBLE -> cryptoCurrenciesCache.let { currentCache ->
            currentCache ?: loadAllCryptoCurrencies(
                orderBy = orderBy,
                orderDirection = orderDirection,
                offset = 0,
                tags = tags,
                timePeriod = timePeriod
            ).let { newData ->
                newData.also { cryptoCurrenciesCache = it.toMutableList() }
            }
        }
        RefreshType.NEXT_PAGE -> loadAllCryptoCurrencies(
            orderBy = orderBy,
            orderDirection = orderDirection,
            offset = lastCryptoCurrencyOffset + LIMIT,
            tags = tags,
            timePeriod = timePeriod
        ).let { newData ->
            val newCache = (cryptoCurrenciesCache.orEmpty() + newData)
            cryptoCurrenciesCache = newCache.toMutableList()
            newCache
        }
    }

    private suspend fun loadAllCryptoCurrencies(
        orderBy: String,
        orderDirection: String,
        offset: Int,
        tags: List<String>,
        timePeriod: String
    ) = manager.cryptoSource.getAllCryptoCurrencies(
        orderBy = orderBy,
        orderDirection = orderDirection,
        offset = offset,
        tags = tags,
        timePeriod = timePeriod
    ).body()?.data?.coins?.mapNotNull { currencyResponse ->
        currencyResponse.toCryptoCurrency()
    }?.distinctBy { it.name }.also {
        lastCryptoCurrencyOffset = offset
    } ?: throw IllegalStateException("Invalid data returned by the server")

    suspend fun getCryptoCurrencyDetails(
        uuid: String,
    ) = manager.cryptoSource.getCryptoCurrencyDetails(
        uuid = uuid
    ).body()?.data?.coin?.toCryptoCurrencyDetails().let { newData ->
        newData.also { cryptoCurrencyDetailsCache = it }
    } ?: throw IllegalStateException("Invalid data returned by the server")

    suspend fun getCryptoCurrencyHistory(
        uuid: String,
        timePeriod: String,
    ) = manager.cryptoSource.getCryptoCurrencyHistory(
        uuid = uuid,
        timePeriod = timePeriod
    ).body()?.data?.history?.mapNotNull {
        it.toCryptoHistoryItem()
    }.let { newData ->
        newData.also { cryptoCurrencyHistoryCache = it?.toMutableList() }
    } ?: throw IllegalStateException("Invalid data returned by the server")
}
