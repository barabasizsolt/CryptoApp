package com.example.cryptoapp.firestore.useCase
import com.example.cryptoapp.firestore.service.FirestoreService

class DeleteCryptoCurrencyFromWatchListUseCase(private val service: FirestoreService) {

    operator fun invoke(id: String) = service.deleteCryptoCurrencyFromWatchList(id = id)
}