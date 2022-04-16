package com.example.cryptoapp.data.repository.cryptocurrency

import com.example.cryptoapp.data.shared.toModel
import com.example.cryptoapp.data.source.CryptoCurrencySource
import java.lang.IllegalStateException

class CryptoCurrencyDetailsRepository(private val source: CryptoCurrencySource) {

    suspend fun getCryptoCurrencyDetails(
        uuid: String,
    ) = source.cryptoCurrencySource.getCryptoCurrencyDetails(
        uuid = uuid
    ).body()?.data?.singleCryptoCurrencyDetails?.toModel() ?: throw IllegalStateException("Invalid data returned by the server")
}
