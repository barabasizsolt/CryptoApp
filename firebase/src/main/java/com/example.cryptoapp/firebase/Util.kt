package com.example.cryptoapp.firebase

sealed class FirebaseResult<T> {

    data class Success<T>(val data: T) : FirebaseResult<T>()

    data class Failure<T>(val exception: Throwable) : FirebaseResult<T>()
}

inline fun <T> wrapToResult(function: () -> T): FirebaseResult<T> = try {
    FirebaseResult.Success(function())
} catch (exception: Exception) {
    FirebaseResult.Failure(exception)
}