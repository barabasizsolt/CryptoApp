package com.example.cryptoapp.data.repository.cryptocurrency

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.shared.toCryptoCurrencyDetails
import java.lang.IllegalStateException

class CryptoCurrencyDetailsRepository(private val manager: NetworkManager) {

    suspend fun getCryptoCurrencyDetails(
        uuid: String,
    ) = manager.cryptoSource.getCryptoCurrencyDetails(
        uuid = uuid
    ).body()?.data?.coin?.toCryptoCurrencyDetails() ?: throw IllegalStateException("Invalid data returned by the server")
}
