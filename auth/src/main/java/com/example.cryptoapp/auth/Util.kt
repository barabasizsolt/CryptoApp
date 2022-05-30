package com.example.cryptoapp.auth

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose

sealed class AuthResult {

    object Success : AuthResult()

    data class Failure(val error: String) : AuthResult()
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> ProducerScope<AuthResult>.consumeTask(task: Task<T>) {
    task
        .addOnSuccessListener { offer(element = AuthResult.Success) }
        .addOnFailureListener { error -> offer(element = AuthResult.Failure(error = error.message.orEmpty()))  }
    awaitClose { task }
}

