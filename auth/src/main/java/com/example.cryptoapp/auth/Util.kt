package com.example.cryptoapp.auth

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.experimental.ExperimentalTypeInference

sealed class AuthResult {

    object Success : AuthResult()

    data class Failure(val error: String) : AuthResult()
}

@OptIn(ExperimentalTypeInference::class, ExperimentalCoroutinesApi::class)
fun <T>consumeTask(task: Task<T>): Flow<AuthResult> = callbackFlow {
    task
        .addOnSuccessListener { offer(element = AuthResult.Success) }
        .addOnFailureListener { error -> offer(element = AuthResult.Failure(error = error.message.orEmpty()))  }
    awaitClose { }
}

