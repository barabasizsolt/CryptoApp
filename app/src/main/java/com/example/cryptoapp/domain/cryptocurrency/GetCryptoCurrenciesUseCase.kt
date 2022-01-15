package com.example.cryptoapp.domain.cryptocurrency

import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyRepository
import com.example.cryptoapp.domain.resultOf

class GetCryptoCurrenciesUseCase(private val repository: CryptoCurrencyRepository) {

    suspend operator fun invoke(
        orderBy: String,
        orderDirection: String,
        tags: List<String>,
        timePeriod: String,
        refreshType: RefreshType
    ) = resultOf {
        repository.getAllCryptoCurrencies(
            orderBy = orderBy,
            orderDirection = orderDirection,
            tags = tags,
            timePeriod = timePeriod,
            refreshType = refreshType
        )
    }
}
