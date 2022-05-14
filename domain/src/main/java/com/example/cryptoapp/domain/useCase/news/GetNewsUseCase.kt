package com.example.cryptoapp.domain.useCase.news

import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.repository.news.NewsRepository
import com.example.cryptoapp.domain.resultOf

class GetNewsUseCase(private val repository: NewsRepository) {

    suspend operator fun invoke(refreshType: RefreshType) = resultOf {
        repository.getAllNews(refreshType)
    }
}
