package com.example.cryptoapp.data.service

import com.example.cryptoapp.data.model.response.news.AllNewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsService {
    @GET("news")
    suspend fun getNews(
        @Header("accept") key: String = "application/json",
        @Query("page") page: String,
    ): Response<AllNewsResponse>
}
