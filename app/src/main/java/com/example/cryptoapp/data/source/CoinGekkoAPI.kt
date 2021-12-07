package com.example.cryptoapp.data.source

import com.example.cryptoapp.data.model.event.AllEvents
import com.example.cryptoapp.data.model.exchange.Exchange
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface CoinGekkoAPI {
    @GET("exchanges")
    suspend fun getExchanges(
        @Header("accept") key : String = "application/json",
        @Query("per_page") perPage : Int,
        @Query("page") page : String,
    ): Response<List<Exchange>>

    @GET("events")
    suspend fun getEvents(
        @Header("accept") key : String = "application/json",
        @Query("page") page : String,
    ): Response<AllEvents>
}