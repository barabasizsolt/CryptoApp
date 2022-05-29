package com.example.cryptoapp.firebase.domain.firestore

import com.example.cryptoapp.firebase.data.firestore.FirestoreRepository

class DeleteCryptoCurrencyFromWatchList(private val repository: FirestoreRepository) {

    operator fun invoke(id: String, userId: String) = repository.deleteCryptoCurrencyFromWatchList(id = id, userId = userId)
}