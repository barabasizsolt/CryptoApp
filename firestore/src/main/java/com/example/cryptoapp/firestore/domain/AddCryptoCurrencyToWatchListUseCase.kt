package com.example.cryptoapp.firestore.domain

import com.example.cryptoapp.firestore.data.FirestoreService

class AddCryptoCurrencyToWatchListUseCase(private val service: FirestoreService) {

    operator fun invoke(id: String, userId: String) = service.addCryptoCurrencyToWatchList(id = id, userId = userId)
}