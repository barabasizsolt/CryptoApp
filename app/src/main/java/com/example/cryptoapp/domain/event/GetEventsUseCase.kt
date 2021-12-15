package com.example.cryptoapp.domain.event

import com.example.cryptoapp.data.repository.EventRepository
import com.example.cryptoapp.util.resultOf

class GetEventsUseCase(private val repository: EventRepository) {

    suspend operator fun invoke(page: String) = resultOf {
        repository.getAllEvents(page = page)
    }
}
