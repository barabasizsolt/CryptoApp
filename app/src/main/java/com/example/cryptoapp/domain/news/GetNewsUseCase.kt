package com.example.cryptoapp.domain.news

import com.example.cryptoapp.data.repository.NewsRepository
import com.example.cryptoapp.util.resultOf

class GetNewsUseCase(private val repository: NewsRepository) {

    suspend operator fun invoke(page: String) = resultOf {
        repository.getAllNews(page = page)
    }
}
