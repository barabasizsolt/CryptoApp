package com.example.cryptoapp.data.repository.category

import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.model.category.Category
import com.example.cryptoapp.data.shared.toModel
import com.example.cryptoapp.data.source.CategorySource
import java.lang.IllegalStateException

class CategoryRepository(private val source: CategorySource) {

    private var cache: MutableList<Category>? = null

    suspend fun getAllCategories(refreshType: RefreshType): List<Category> = when (refreshType) {
        RefreshType.FORCE_REFRESH -> loadData().also { cache = it.toMutableList() }
        else -> cache ?: loadData().also { cache = it.toMutableList() }
    }

    private suspend fun loadData() =
        source.categorySource.getCategories().body()?.mapNotNull {
            it.toModel()
        } ?: throw IllegalStateException("Invalid data returned by the server")
}
