package com.example.cryptoapp.domain

import com.example.cryptoapp.data.repository.CryptoRepository
import com.example.cryptoapp.utils.resultOf

class GetCryptoCurrenciesUseCase(private val repository: CryptoRepository) {

    suspend operator fun invoke(
        orderBy: String,
        orderDirection: String,
        offset: Int,
        tags: Set<String>,
        timePeriod: String
    ) = resultOf {
        repository.getAllCryptoCurrencies(
            orderBy = orderBy, orderDirection = orderDirection, offset = offset, tags = tags, timePeriod = timePeriod
        )
    }
}
