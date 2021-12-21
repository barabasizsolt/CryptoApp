package com.example.cryptoapp.domain

import com.example.cryptoapp.data.model.Result

inline fun <T> resultOf(function: () -> T): Result<T> = try {
    Result.Success(function())
} catch (exception: Exception) {
    Result.Failure(exception)
}
