package com.example.cryptoapp.data.repository.cryptocurrency

import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.cryptocurrency.CryptoCurrency
import com.example.cryptoapp.data.shared.toModel
import com.example.cryptoapp.data.source.CryptoCurrencySource
import java.lang.IllegalStateException

class CryptoCurrencyWatchListRepository(private val source: CryptoCurrencySource) {

    companion object {
        const val LIMIT = 50
    }

    private var cryptoCurrenciesCache: MutableList<CryptoCurrency>? = null
    private var lastCryptoCurrencyOffset = 0

    suspend fun getCryptoCurrenciesForWatchlist(
        uuids: List<String>,
        refreshType: RefreshType
    ): List<CryptoCurrency> = when (refreshType) {
        RefreshType.FORCE_REFRESH -> loadCryptoCurrenciesForWatchList(
            offset = 0,
            uuids = uuids
        ).let { newData ->
            newData.also { cryptoCurrenciesCache = it.toMutableList() }
        }
        RefreshType.CACHE_IF_POSSIBLE -> cryptoCurrenciesCache.let { currentCache ->
            currentCache ?: loadCryptoCurrenciesForWatchList(
                uuids = uuids,
                offset = 0
            ).let { newData ->
                newData.also { cryptoCurrenciesCache = it.toMutableList() }
            }
        }
        RefreshType.NEXT_PAGE -> loadCryptoCurrenciesForWatchList(
            offset = lastCryptoCurrencyOffset + LIMIT,
            uuids = uuids
        ).let { newData ->
            val newCache = (cryptoCurrenciesCache.orEmpty() + newData)
            cryptoCurrenciesCache = newCache.toMutableList()
            newCache
        }
    }

    private suspend fun loadCryptoCurrenciesForWatchList(
        offset: Int,
        uuids: List<String>
    ) = source.cryptoCurrencySource.getCryptoCurrenciesForWatchList(
        offset = offset,
        uuids = uuids
    ).body()?.data?.coins?.mapNotNull { currencyResponse ->
        currencyResponse.toModel()
    }?.distinctBy { it.name }.also {
        lastCryptoCurrencyOffset = offset
    } ?: throw IllegalStateException("Invalid data returned by the server")
}