package com.example.cryptoapp.firebase.domain.firestore

import com.example.cryptoapp.firebase.data.firestore.FirestoreRepository

class AddCryptoCurrencyToWatchListUseCase(private val repository: FirestoreRepository) {

    operator fun invoke(id: String, userId: String) = repository.addCryptoCurrencyToWatchList(id = id, userId = userId)
}