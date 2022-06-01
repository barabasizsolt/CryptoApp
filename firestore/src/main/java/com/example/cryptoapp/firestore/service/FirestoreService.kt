package com.example.cryptoapp.firestore.service

import kotlinx.coroutines.flow.Flow

interface FirestoreService {

    fun addCryptoCurrencyToWatchList(id: String)

    fun deleteCryptoCurrencyFromWatchList(id: String)

    fun getCryptoCurrenciesInWatchList(): Flow<List<String>?>

    fun isCryptoCurrencyAddedToWatchList(id: String): Flow<Boolean?>
}