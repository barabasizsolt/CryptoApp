package com.example.cryptoapp.domain.cryptocurrency

import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyDetailsRepository
import com.example.cryptoapp.domain.resultOf

class GetCryptoCurrencyDetailsUseCase(private val repository: CryptoCurrencyDetailsRepository) {

    suspend operator fun invoke(uuid: String) = resultOf {
        repository.getCryptoCurrencyDetails(uuid = uuid)
    }
}
