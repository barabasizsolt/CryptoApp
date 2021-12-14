package com.example.cryptoapp.data.service

import com.example.cryptoapp.data.model.event.AllEvents
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface EventService {
    @GET("events")
    suspend fun getEvents(
        @Header("accept") key: String = "application/json",
        @Query("page") page: String,
    ): Response<AllEvents>
}