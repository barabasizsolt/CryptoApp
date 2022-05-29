package com.example.cryptoapp.firebase.data.firestore

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface FirestoreService {

    fun addCryptoCurrencyToWatchList(id: String, userId: String): Task<Void>

    fun deleteCryptoCurrencyFromWatchList(id: String, userId: String): Task<Void>

    fun getCryptoCurrency(id: String, userId: String): Task<DocumentSnapshot>

    fun getAllCryptoCurrency(userId: String): Task<QuerySnapshot>
}