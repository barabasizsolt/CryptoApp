package com.example.cryptoapp.domain.useCase.category

import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.repository.category.CategoryRepository
import com.example.cryptoapp.domain.resultOf

class GetCategoriesUseCase(private val repository: CategoryRepository) {

    suspend operator fun invoke(refreshType: RefreshType) = resultOf {
        repository.getAllCategories(refreshType = refreshType)
    }
}
