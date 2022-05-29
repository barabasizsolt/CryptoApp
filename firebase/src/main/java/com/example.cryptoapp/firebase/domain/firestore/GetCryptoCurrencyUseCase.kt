package com.example.cryptoapp.firebase.domain.firestore

import com.example.cryptoapp.firebase.data.firestore.FirestoreRepository

class GetCryptoCurrencyUseCase(private val repository: FirestoreRepository) {

    operator fun invoke(id: String, userId: String) = repository.getCryptoCurrency(id = id, userId = userId)
}