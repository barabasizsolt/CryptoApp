package com.example.cryptoapp.firestore.domain
import com.example.cryptoapp.firestore.data.FirestoreService

class DeleteCryptoCurrencyFromWatchList(private val service: FirestoreService) {

    operator fun invoke(id: String, userId: String) = service.deleteCryptoCurrencyFromWatchList(id = id, userId = userId)
}