package com.example.cryptoapp.util

inline fun <T> resultOf(function: () -> T): Result<T> = try {
    Result.Success(function())
} catch (exception: Exception) {
    Result.Failure(exception)
}
