package com.example.cryptoapp.data.repository.cryptocurrency

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.shared.toCryptoCurrencyHistory
import java.lang.IllegalStateException

class CryptoCurrencyHistoryRepository(private val manager: NetworkManager) {

    suspend fun getCryptoCurrencyHistory(
        uuid: String,
        timePeriod: String,
    ) = manager.cryptoSource.getCryptoCurrencyHistory(
        uuid = uuid,
        timePeriod = timePeriod
    ).body()?.data?.history?.mapNotNull {
        it.toCryptoCurrencyHistory()
    } ?: throw IllegalStateException("Invalid data returned by the server")
}
