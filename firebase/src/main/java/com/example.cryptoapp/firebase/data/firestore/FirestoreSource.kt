package com.example.cryptoapp.firebase.data.firestore

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions

class FirestoreSource : FirestoreService {

    private val firebaseFireStore = FirebaseFirestore.getInstance()

    override fun addCryptoCurrencyToWatchList(id: String, userId: String): Task<Void> =
        firebaseFireStore
            .collection(COLLECTION_PATH)
            .document("$userId-$id")
            .set(
                hashMapOf("id" to id, "userId" to userId),
                SetOptions.merge()
            )

    override fun deleteCryptoCurrencyFromWatchList(id: String, userId: String): Task<Void> =
        firebaseFireStore
            .collection(COLLECTION_PATH)
            .document("$userId-$id")
            .delete()

    override fun getCryptoCurrency(id: String, userId: String): Task<DocumentSnapshot> =
        firebaseFireStore
            .collection(COLLECTION_PATH)
            .document("$userId-$id")
            .get()

    override fun getAllCryptoCurrency(userId: String): Task<QuerySnapshot> =
        firebaseFireStore
            .collection(COLLECTION_PATH)
            .whereEqualTo("userId", userId)
            .get()

    companion object {
        private const val COLLECTION_PATH: String = "cryptoCurrencies"
    }
}