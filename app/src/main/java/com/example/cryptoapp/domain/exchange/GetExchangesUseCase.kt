package com.example.cryptoapp.domain.exchange

import com.example.cryptoapp.data.repository.ExchangeRepository
import com.example.cryptoapp.domain.resultOf

class GetExchangesUseCase(private val repository: ExchangeRepository) {

    suspend operator fun invoke(perPage: Int, page: String) = resultOf {
        repository.getAllExchanges(perPage = perPage, page = page)
    }
}
