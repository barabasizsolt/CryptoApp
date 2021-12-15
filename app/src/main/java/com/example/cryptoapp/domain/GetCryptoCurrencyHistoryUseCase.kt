package com.example.cryptoapp.domain

import com.example.cryptoapp.data.repository.CryptoRepository
import com.example.cryptoapp.util.resultOf

class GetCryptoCurrencyHistoryUseCase(private val repository: CryptoRepository) {

    suspend operator fun invoke(uuid: String, timePeriod: String) = resultOf {
        repository.getCryptoCurrencyHistory(uuid = uuid, timePeriod = timePeriod)
    }
}
