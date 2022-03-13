package com.example.cryptoapp.feature.shared.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

fun <T> MutableSharedFlow<T>.pushEvent(event: T) {
    tryEmit(event)
}

fun <T> eventFlow() = MutableSharedFlow<T>(
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)

fun consume(callback: () -> Unit) = true.also { callback() }
