package com.example.cryptoapp.firestore.useCase

import com.example.cryptoapp.firestore.service.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class GetCryptoCurrenciesInWatchListUseCase(private val service: FirestoreService) {

    operator fun invoke(): Flow<List<String>> = service.getCryptoCurrenciesInWatchList().filterNotNull()
}