package com.example.cryptoapp.data.source

import com.example.cryptoapp.data.retrofitInstance.CoinGekkoRetrofitInstance
import com.example.cryptoapp.data.service.CategoryService

class CategorySource(private val retrofitInstance: CoinGekkoRetrofitInstance) {

    val categorySource: CategoryService by lazy {
        retrofitInstance.coinGekkoRetrofitInstance.create(CategoryService::class.java)
    }
}
