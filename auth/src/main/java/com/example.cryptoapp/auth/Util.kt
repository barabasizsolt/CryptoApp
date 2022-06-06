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

sealed class AuthWithResult<T> {

    data class Success<T>(val data: T) : AuthWithResult<T>()

    data class Failure<T>(val error: String) : AuthWithResult<T>()
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <P, T>consumeTaskWithResult(task: Task<T>, taskConverter: (T) -> P): Flow<AuthWithResult<P>> =  callbackFlow {
    task
        .addOnSuccessListener { result -> offer(element = AuthWithResult.Success(data = taskConverter(result))) }
        .addOnFailureListener { error -> offer(element = AuthWithResult.Failure(error = error.message.orEmpty()))  }
    awaitClose { }
}

