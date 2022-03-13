package com.example.cryptoapp.data.repository.cryptocurrency

import com.example.cryptoapp.data.manager.NetworkManager
import com.example.cryptoapp.data.shared.toModel
import java.lang.IllegalStateException

class CryptoCurrencyDetailsRepository(private val manager: NetworkManager) {

    suspend fun getCryptoCurrencyDetails(
        uuid: String,
    ) = manager.cryptoSource.getCryptoCurrencyDetails(
        uuid = uuid
    ).body()?.data?.singleCryptoCurrencyDetails?.toModel() ?: throw IllegalStateException("Invalid data returned by the server")
}
