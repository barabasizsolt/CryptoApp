package com.example.cryptoapp.domain.useCase.exchange

import com.example.cryptoapp.data.repository.exchange.ExchangeDetailRepository
import com.example.cryptoapp.domain.resultOf

class GetExchangeDetailsUseCase(private val repository: ExchangeDetailRepository) {

    suspend operator fun invoke(id: String) = resultOf {
        repository.getExchangeDetails(id = id)
    }
}