package com.example.cryptoapp.domain.useCase.cryptocurrency

import com.example.cryptoapp.data.model.RefreshType
import com.example.cryptoapp.data.repository.cryptocurrency.CryptoCurrencyWatchListRepository
import com.example.cryptoapp.domain.resultOf

class GetCryptoCurrenciesForWatchListUseCase(private val repository: CryptoCurrencyWatchListRepository) {

    suspend operator fun invoke(
        uuids: List<String>,
        refreshType: RefreshType
    ) = resultOf {
        repository.getCryptoCurrenciesForWatchlist(
            uuids = uuids,
            refreshType = refreshType
        )
    }
}