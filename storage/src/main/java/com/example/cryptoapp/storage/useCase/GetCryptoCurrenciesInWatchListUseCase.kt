package com.example.cryptoapp.storage.useCase

import com.example.cryptoapp.storage.service.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class GetCryptoCurrenciesInWatchListUseCase(private val service: FirestoreService) {

    operator fun invoke(): Flow<List<String>> = service.getCryptoCurrenciesInWatchList().filterNotNull()
}