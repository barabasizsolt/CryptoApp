package com.example.cryptoapp.data.repository

import com.example.cryptoapp.data.NetworkManager
import com.example.cryptoapp.data.model.event.AllEvents
import retrofit2.Response

class EventRepository(private val manager: NetworkManager) {
    suspend fun getAllEvents(page: String): Response<AllEvents> {
        return manager.eventSource.getEvents(page = page)
    }
}
