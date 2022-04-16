package com.example.cryptoapp.data.source

import com.example.cryptoapp.data.service.CategoryService
import com.example.cryptoapp.data.retrofitInstance.CoinGekkoRetrofitInstance

class CategorySource(private val retrofitInstance: CoinGekkoRetrofitInstance) {

    val categorySource: CategoryService by lazy {
        retrofitInstance.coinGekkoRetrofitInstance.create(CategoryService::class.java)
    }
}