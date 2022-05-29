package com.example.cryptoapp.firebase.domain.firestore

import com.example.cryptoapp.firebase.data.firestore.FirestoreRepository

class GetAllCryptoCurrencyUseCase(private val repository: FirestoreRepository) {

    operator fun invoke(userId: String) = repository.getAllCryptoCurrency(userId = userId)
}