package com.example.cryptoapp.data.repository.cryptocurrency

import com.example.cryptoapp.data.shared.toModel
import com.example.cryptoapp.data.source.CryptoCurrencySource
import java.lang.IllegalStateException

class CryptoCurrencyHistoryRepository(private val source: CryptoCurrencySource) {

    suspend fun getCryptoCurrencyHistory(
        uuid: String,
        timePeriod: String,
    ) = source.cryptoCurrencySource.getCryptoCurrencyHistory(
        uuid = uuid,
        timePeriod = timePeriod
    ).body()?.data?.history?.mapNotNull {
        it.toModel()
    } ?: throw IllegalStateException("Invalid data returned by the server")
}
