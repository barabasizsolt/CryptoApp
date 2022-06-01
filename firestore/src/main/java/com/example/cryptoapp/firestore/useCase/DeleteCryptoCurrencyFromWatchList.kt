package com.example.cryptoapp.firestore.useCase
import com.example.cryptoapp.firestore.service.FirestoreService

class DeleteCryptoCurrencyFromWatchList(private val service: FirestoreService) {

    operator fun invoke(id: String) = service.deleteCryptoCurrencyFromWatchList(id = id)
}