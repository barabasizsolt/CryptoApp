package com.example.cryptoapp.firestore.useCase

import com.example.cryptoapp.firestore.service.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class IsCryptoCurrencyAddedToWatchList(private val service: FirestoreService) {

    operator fun invoke(id: String): Flow<Boolean> = service.isCryptoCurrencyAddedToWatchList(id = id).filterNotNull()
}