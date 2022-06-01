package com.example.cryptoapp.firestore

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@OptIn(ExperimentalCoroutinesApi::class)
fun <T>consumeQuery(query: Query, snapshotToFlowConverter: (QuerySnapshot) -> T): Flow<T> = callbackFlow {
    val listener = query.addSnapshotListener { snapshot, _ ->
        if (snapshot == null) { return@addSnapshotListener }
        offer(element = snapshotToFlowConverter(snapshot) )
    }
    awaitClose { listener.remove() }
}