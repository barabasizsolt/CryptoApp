package com.example.cryptoapp.firestore.domain

import com.example.cryptoapp.firestore.data.CryptoAndUserId
import com.example.cryptoapp.firestore.data.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class GetCryptoCurrencyUseCase(private val service: FirestoreService) {

    operator fun invoke(id: String, userId: String): Flow<List<CryptoAndUserId>> = service.cryptoCurrencyIdsFlow.filterNotNull().map {
        it.filter { cryptoAndUserId -> cryptoAndUserId.id == id && cryptoAndUserId.userId == userId }
    }
}