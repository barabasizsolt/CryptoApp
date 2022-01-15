package com.example.cryptoapp.data.repository.cryptocurrency

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.cryptoCurrency.CryptoCurrency
import com.example.cryptoapp.data.shared.toCryptoCurrency
import java.lang.IllegalStateException

class CryptoCurrencyRepository(private val manager: NetworkManager) {

    companion object {
        const val LIMIT = 50
    }

    private var cryptoCurrenciesCache: MutableList<CryptoCurrency>? = null
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
}
