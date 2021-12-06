package com.example.cryptoapp.api.cryptocurrencies

import com.example.cryptoapp.model.allcryptocurrencies.AllCryptoCurrencies
import com.example.cryptoapp.model.cryptocurrencydetail.CryptoCurrencyDetails
import com.example.cryptoapp.model.cryptocurrencydetail.CryptoCurrencyHistory
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