package com.example.cryptoapp.domain

import com.example.cryptoapp.data.repository.CryptoRepository
import com.example.cryptoapp.util.resultOf

class GetCryptoCurrencyDetailsUseCase(private val repository: CryptoRepository) {

    suspend operator fun invoke(uuid: String) = resultOf {
        repository.getCryptoCurrencyDetails(uuid = uuid)
    }
}
