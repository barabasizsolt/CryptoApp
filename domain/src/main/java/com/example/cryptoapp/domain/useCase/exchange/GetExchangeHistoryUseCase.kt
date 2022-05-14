package com.example.cryptoapp.domain.useCase.exchange

import com.example.cryptoapp.data.repository.exchange.ExchangeHistoryRepository
import com.example.cryptoapp.domain.resultOf

class GetExchangeHistoryUseCase(private val repository: ExchangeHistoryRepository) {

    suspend operator fun invoke(id: String, days: Int = 7) = resultOf {
        repository.getExchangeHistory(id = id, days = days)
    }
}