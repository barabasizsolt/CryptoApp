package com.example.cryptoapp.data.service

import com.example.cryptoapp.data.model.exchange.Exchange
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ExchangeService {
    @GET("exchanges")
    suspend fun getExchanges(
        @Header("accept") key: String = "application/json",
        @Query("per_page") perPage: Int,
        @Query("page") page: String,
    ): Response<List<Exchange>>
}
