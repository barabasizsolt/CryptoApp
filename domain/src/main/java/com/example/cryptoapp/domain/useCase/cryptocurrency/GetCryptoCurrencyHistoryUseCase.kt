package com.example.cryptoapp.domain.useCase.cryptocurrency

import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyHistoryRepository
import com.example.cryptoapp.domain.resultOf

class GetCryptoCurrencyHistoryUseCase(private val repository: CryptoCurrencyHistoryRepository) {

    suspend operator fun invoke(uuid: String, timePeriod: String) = resultOf {
        repository.getCryptoCurrencyHistory(uuid = uuid, timePeriod = timePeriod)
    }
}
