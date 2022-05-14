package com.example.cryptoapp.data.service

import com.example.cryptoapp.data.service.response.exchange.ExchangeDetailHistoryResponse
import com.example.cryptoapp.data.service.response.exchange.ExchangeDetailResponse
import com.example.cryptoapp.data.service.response.exchange.ExchangeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ExchangeService {
    @GET("exchanges")
    suspend fun getExchanges(
        @Header("accept") key: String = "application/json",
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): Response<List<ExchangeResponse>>

    @GET("exchanges/{id}")
    suspend fun getExchangeDetails(
        @Header("accept") key: String = "application/json",
        @Path("id") id: String
    ): Response<ExchangeDetailResponse>

    @GET("exchanges/{id}/volume_chart")
    suspend fun getExchangeHistory(
        @Header("accept") key: String = "application/json",
        @Path("id") id: String,
        @Query("days") days: Int
    ): Response<ExchangeDetailHistoryResponse>
}
