package com.example.cryptoapp.firestore.useCase

import com.example.cryptoapp.firestore.service.model.CryptoAndUserId
import com.example.cryptoapp.firestore.service.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class GetCryptoCurrencyUseCase(private val service: FirestoreService) {

    operator fun invoke(id: String, userId: String): Flow<List<CryptoAndUserId>> = service.cryptoCurrencyIdsFlow.filterNotNull().map {
        it.filter { cryptoAndUserId -> cryptoAndUserId.id == id && cryptoAndUserId.userId == userId }
    }
}