package com.example.cryptoapp.firebase.data.firestore

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class FirestoreRepository(private val source: FirestoreSource) {

    fun addCryptoCurrencyToWatchList(id: String, userId: String): Task<Void> =
        source.addCryptoCurrencyToWatchList(id = id, userId = userId)

    fun deleteCryptoCurrencyFromWatchList(id: String, userId: String): Task<Void> =
        source.deleteCryptoCurrencyFromWatchList(id = id, userId = userId)

    fun getCryptoCurrency(id: String, userId: String): Task<DocumentSnapshot> =
        source.getCryptoCurrency(id = id, userId = userId)

    fun getAllCryptoCurrency(userId: String): Task<QuerySnapshot> =
        source.getAllCryptoCurrency(userId = userId)
}