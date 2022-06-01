package com.example.cryptoapp.firestore.useCase

import com.example.cryptoapp.firestore.service.FirestoreService

class AddCryptoCurrencyToWatchListUseCase(private val service: FirestoreService) {

    operator fun invoke(id: String) = service.addCryptoCurrencyToWatchList(id = id)
}