package com.example.cryptoapp.firestore.service

import com.example.cryptoapp.auth.service.AuthenticationService
import com.example.cryptoapp.firestore.consumeQuery
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class FirestoreServiceImpl(private val authService: AuthenticationService) : FirestoreService {

    private lateinit var collection: CollectionReference

    override fun initialize() {
        collection = FirebaseFirestore.getInstance().collection(COLLECTION_PATH)
    }

    override fun addCryptoCurrencyToWatchList(id: String) {
        collection
            .document("${authService.getCurrentUser()?.userId}-$id")
            .set(hashMapOf(ID to id, USER_ID to authService.getCurrentUser()?.userId), SetOptions.merge())
    }

    override fun deleteCryptoCurrencyFromWatchList(id: String) {
        collection
            .document("${authService.getCurrentUser()?.userId}-$id")
            .delete()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCryptoCurrenciesInWatchList(): Flow<List<String>?> = consumeQuery(
        query = collection.whereEqualTo(USER_ID, authService.getCurrentUser()?.userId),
        queryConverter = { snapshot ->
            snapshot.documents.map { document -> document?.data?.get(ID).toString() }
        }
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun isCryptoCurrencyAddedToWatchList(id: String): Flow<Boolean?> = consumeQuery(
        query = collection
            .whereEqualTo(USER_ID, authService.getCurrentUser()?.userId)
            .whereEqualTo(ID, id),
        queryConverter = { snapshot ->
            snapshot.documents.isNotEmpty()
        }
    )

    companion object {
        private const val COLLECTION_PATH: String = "cryptoCurrencies"
        private const val ID: String = "id"
        private const val USER_ID: String = "userId"
    }
}