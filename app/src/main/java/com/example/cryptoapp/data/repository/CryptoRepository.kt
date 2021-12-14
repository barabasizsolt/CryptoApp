package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.model.cryptoCurrency.AllCryptoCurrencies
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoCurrencyHistory
import com.example.cryptoapp.data.NetworkManager
import retrofit2.Response

class CryptoRepository(private val manager: NetworkManager) {
    suspend fun getAllCryptoCurrencies(orderBy: String, orderDirection: String, offset: Int, tags: Set<String>, timePeriod: String): Response<AllCryptoCurrencies> {
        return manager.cryptoSource.getAllCryptoCurrencies(orderBy = orderBy, orderDirection = orderDirection, offset = offset, tags = tags, timePeriod = timePeriod)
    }

    suspend fun getCryptoCurrencyDetails(uuid: String): Response<CryptoCurrencyDetails> {
        return manager.cryptoSource.getCryptoCurrencyDetails(uuid = uuid)
    }

    suspend fun getCryptoCurrencyHistory(uuid: String, timePeriod: String): Response<CryptoCurrencyHistory> {
        return manager.cryptoSource.getCryptoCurrencyHistory(uuid = uuid, timePeriod = timePeriod)
    }
}
