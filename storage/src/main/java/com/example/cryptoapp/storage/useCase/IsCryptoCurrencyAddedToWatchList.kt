package com.example.cryptoapp.storage.useCase

import com.example.cryptoapp.storage.service.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class IsCryptoCurrencyAddedToWatchList(private val service: FirestoreService) {

    operator fun invoke(id: String): Flow<Boolean> = service.isCryptoCurrencyAddedToWatchList(id = id).filterNotNull()
}