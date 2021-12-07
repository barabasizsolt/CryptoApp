package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.source.CryptoRetrofitInstance
import com.example.cryptoapp.data.model.cryptoCurrency.AllCryptoCurrencies
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoCurrencyDetails
import com.example.cryptoapp.data.model.cryptoCurrencyDetail.CryptoCurrencyHistory
import retrofit2.Response

class CryptoApiRepository {
    suspend fun getAllCryptoCurrencies(orderBy : String, orderDirection : String, offset : Int, tags : Set<String>, timePeriod: String) : Response<AllCryptoCurrencies> {
        return CryptoRetrofitInstance.api.getAllCryptoCurrencies(orderBy = orderBy, orderDirection = orderDirection, offset = offset, tags = tags, timePeriod = timePeriod)
    }

    suspend fun getCryptoCurrencyDetails(uuid : String) : Response<CryptoCurrencyDetails> {
        return CryptoRetrofitInstance.api.getCryptoCurrencyDetails(uuid = uuid)
    }

    suspend fun getCryptoCurrencyHistory(uuid : String, timePeriod : String) : Response<CryptoCurrencyHistory> {
        return CryptoRetrofitInstance.api.getCryptoCurrencyHistory(uuid = uuid, timePeriod = timePeriod)
    }
}