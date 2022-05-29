package com.example.cryptoapp.firestore.data

import kotlinx.coroutines.flow.Flow

interface FirestoreService {

    val cryptoCurrencyIdsFlow: Flow<List<CryptoAndUserId>?>

    fun addCryptoCurrencyToWatchList(id: String, userId: String)

    fun deleteCryptoCurrencyFromWatchList(id: String, userId: String)
}