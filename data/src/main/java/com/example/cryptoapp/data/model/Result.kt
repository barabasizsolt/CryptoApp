package com.example.cryptoapp.data.model

/**
 * Represents the result of simple API requests that either fail or succeed.
 */
sealed class Result<T> {

    /**
     * The request has been successful and returned a valid data.
     */
    data class Success<T>(val data: T) : Result<T>()

    /**
     * The request has not been successful as an exception has been thrown.
     */
    data class Failure<T>(val exception: Throwable) : Result<T>()
}
