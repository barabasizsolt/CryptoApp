package com.example.cryptoapp.storage.useCase

import com.example.cryptoapp.storage.service.FirestoreService

class AddCryptoCurrencyToWatchListUseCase(private val service: FirestoreService) {

    operator fun invoke(id: String) = service.addCryptoCurrencyToWatchList(id = id)
}