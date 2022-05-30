package com.example.cryptoapp.firestore.service

import com.example.cryptoapp.firestore.service.model.CryptoAndUserId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreServiceImpl : FirestoreService {

    private val collection = FirebaseFirestore.getInstance().collection(COLLECTION_PATH)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _cryptoCurrencyIdsFlow: Flow<List<CryptoAndUserId>?> = callbackFlow {
        val subscription = collection.addSnapshotListener { snapshot, _ ->
            offer(snapshot?.documents?.map {
                CryptoAndUserId(
                    id = it.data?.get("id").toString(),
                    userId = it.data?.get("userId").toString()
                )
            })
        }
        awaitClose { subscription.remove() }
    }

    override val cryptoCurrencyIdsFlow: Flow<List<CryptoAndUserId>?> = _cryptoCurrencyIdsFlow

    override fun addCryptoCurrencyToWatchList(id: String, userId: String) {
        collection
            .document("$userId-$id")
            .set(hashMapOf("id" to id, "userId" to userId), SetOptions.merge())
    }

    override fun deleteCryptoCurrencyFromWatchList(id: String, userId: String) {
        collection
            .document("$userId-$id")
            .delete()
    }

    companion object {
        private const val COLLECTION_PATH: String = "cryptoCurrencies"
    }
}