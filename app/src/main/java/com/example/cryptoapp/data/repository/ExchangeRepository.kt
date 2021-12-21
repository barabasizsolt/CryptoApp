package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.constant.ExchangeConstant.DEFAULT_PAGE
import com.example.cryptoapp.data.constant.ExchangeConstant.PER_PAGE
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.exchange.Exchange
import com.example.cryptoapp.data.shared.toExchange
import java.lang.IllegalStateException

class ExchangeRepository(private val manager: NetworkManager) {
    private var cache: MutableList<Exchange>? = null
    private var lastDownloadedPage = DEFAULT_PAGE

    suspend fun getAllExchanges(refreshType: RefreshType): List<Exchange> = when (refreshType) {
        RefreshType.FORCE_REFRESH -> loadData(DEFAULT_PAGE).let { exchanges ->
            exchanges.also { cache = it.toMutableList() }
        }
        RefreshType.CACHE_IF_POSSIBLE -> cache.let { currentCache ->
            currentCache ?: loadData(DEFAULT_PAGE).let { exchanges ->
                exchanges.also { cache = it.toMutableList() }
            }
        }
        RefreshType.NEXT_PAGE -> loadData(lastDownloadedPage + 1).let { exchanges ->
            val newCache = (cache.orEmpty() + exchanges).toMutableList()
            cache = newCache
            newCache
        }
    }

    private suspend fun loadData(page: Int) =
        manager.exchangeSource.getExchanges(
            perPage = PER_PAGE,
            page = page
        ).body()?.mapNotNull { exchangeResponse ->
            exchangeResponse.toExchange()
        }?.also {
            lastDownloadedPage = page
        } ?: throw IllegalStateException("Invalid data returned by the server")
}
