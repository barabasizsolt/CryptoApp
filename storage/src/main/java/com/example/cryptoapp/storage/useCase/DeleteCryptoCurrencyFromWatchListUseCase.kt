package com.example.cryptoapp.storage.useCase
import com.example.cryptoapp.storage.service.FirestoreService

class DeleteCryptoCurrencyFromWatchListUseCase(private val service: FirestoreService) {

    operator fun invoke(id: String) = service.deleteCryptoCurrencyFromWatchList(id = id)
}