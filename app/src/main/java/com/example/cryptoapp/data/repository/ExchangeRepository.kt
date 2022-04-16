package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.exchange.Exchange
import com.example.cryptoapp.data.shared.toModel
import com.example.cryptoapp.data.source.ExchangeSource
import java.lang.IllegalStateException

class ExchangeRepository(private val source: ExchangeSource) {

    companion object {
        const val PER_PAGE: Int = 50
    }

    private var cache: MutableList<Exchange>? = null
    private var lastDownloadedPage = 1

    suspend fun getAllExchanges(refreshType: RefreshType): List<Exchange> = when (refreshType) {
        RefreshType.FORCE_REFRESH -> loadData(1).let { exchanges ->
            exchanges.also { cache = it.toMutableList() }
        }
        RefreshType.CACHE_IF_POSSIBLE -> cache.let { currentCache ->
            currentCache ?: loadData(1).let { exchanges ->
                exchanges.also { cache = it.toMutableList() }
            }
        }
        RefreshType.NEXT_PAGE -> loadData(lastDownloadedPage + 1).let { exchanges ->
            val newCache = (cache.orEmpty() + exchanges)
            cache = newCache.toMutableList()
            newCache
        }
    }

    private suspend fun loadData(page: Int) =
        source.exchangeSource.getExchanges(
            perPage = PER_PAGE,
            page = page
        ).body()?.mapNotNull { exchangeResponse ->
            exchangeResponse.toModel()
        }?.also {
            lastDownloadedPage = page
        } ?: throw IllegalStateException("Invalid data returned by the server")
}
