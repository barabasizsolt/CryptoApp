package com.example.cryptoapp.storage.service

import kotlinx.coroutines.flow.Flow

interface FirestoreService {

    fun initialize()

    fun addCryptoCurrencyToWatchList(id: String)

    fun deleteCryptoCurrencyFromWatchList(id: String)

    fun getCryptoCurrenciesInWatchList(): Flow<List<String>?>

    fun isCryptoCurrencyAddedToWatchList(id: String): Flow<Boolean?>
}