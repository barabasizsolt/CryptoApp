package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.shared.toEvent
import java.lang.IllegalStateException

class EventRepository(private val manager: NetworkManager) {
    suspend fun getAllEvents(page: String) =
        manager.eventSource.getEvents(page = page).body()?.data?.map { eventResponse ->
            eventResponse.toEvent()
        } ?: throw IllegalStateException("Invalid data returned by the server")
}
