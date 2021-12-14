package com.example.cryptoapp.utils

suspend inline fun <T> resultOf(function: suspend () -> T): Result<T> = try {
    Result.Success(function())
} catch (exception: Exception) {
    Result.Failure(exception)
}
