package com.example.cryptoapp.data.service

import com.example.cryptoapp.data.model.response.categories.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CategoriesService {
    @GET("coins/categories")
    suspend fun getCategories(
        @Header("accept") key: String = "application/json",
        @Query("order") order: String = "market_cap_desc"
    ): Response<List<CategoryResponse>>
}
