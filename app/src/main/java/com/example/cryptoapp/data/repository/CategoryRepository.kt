package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.manager.NetworkManager
import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.category.Category
import com.example.cryptoapp.data.shared.toModel
import java.lang.IllegalStateException

class CategoryRepository(private val manager: NetworkManager) {

    private var cache: MutableList<Category>? = null

    suspend fun getAllCategories(refreshType: RefreshType): List<Category> = when (refreshType) {
        RefreshType.FORCE_REFRESH -> loadData().also { cache = it.toMutableList() }
        else -> cache ?: loadData().also { cache = it.toMutableList() }
    }

    private suspend fun loadData() =
        manager.categorySource.getCategories().body()?.mapNotNull {
            it.toModel()
        } ?: throw IllegalStateException("Invalid data returned by the server")
}