package com.example.cryptoapp.data.service

import com.example.cryptoapp.data.service.response.cryptocurrency.all.AllCryptoCurrenciesResponse
import com.example.cryptoapp.data.service.response.cryptocurrency.detail.CryptoCurrencyDetailsResponse
import com.example.cryptoapp.data.service.response.cryptocurrency.history.CryptoCurrencyHistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoCurrencyService {
    @GET("coins")
    suspend fun getAllCryptoCurrencies(
        @Header("x-access-token") key: String = "coinrankingd228a6852a6d7ca4c14c25076fdb42f54138843c128f440c",
        @Query("orderBy") orderBy: String,
        @Query("orderDirection") orderDirection: String,
        @Query("offset") offset: Int,
        @Query("tags[]") tags: List<String>,
        @Query("timePeriod") timePeriod: String,
    ): Response<AllCryptoCurrenciesResponse>

    @GET("coins")
    suspend fun getCryptoCurrenciesForWatchList(
        @Header("x-access-token") key: String = "coinrankingd228a6852a6d7ca4c14c25076fdb42f54138843c128f440c",
        @Query("offset") offset: Int,
        @Query("uuids[]") uuids: List<String>
    ): Response<AllCryptoCurrenciesResponse>

    @GET("coin/{uuid}")
    suspend fun getCryptoCurrencyDetails(
        @Header("x-access-token") key: String = "coinrankingd228a6852a6d7ca4c14c25076fdb42f54138843c128f440c",
        @Path("uuid") uuid: String
    ): Response<CryptoCurrencyDetailsResponse>

    @GET("coin/{uuid}/history")
    suspend fun getCryptoCurrencyHistory(
        @Header("x-access-token") key: String = "coinrankingd228a6852a6d7ca4c14c25076fdb42f54138843c128f440c",
        @Path("uuid") uuid: String,
        @Query("timePeriod") timePeriod: String,
    ): Response<CryptoCurrencyHistoryResponse>
}
