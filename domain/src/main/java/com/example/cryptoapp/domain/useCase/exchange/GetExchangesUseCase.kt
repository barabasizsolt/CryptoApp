package com.example.cryptoapp.domain.useCase.exchange

import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.repository.exchange.ExchangeRepository
import com.example.cryptoapp.domain.resultOf

class GetExchangesUseCase(private val repository: ExchangeRepository) {

    suspend operator fun invoke(refreshType: RefreshType) = resultOf {
        repository.getAllExchanges(refreshType)
    }
}
