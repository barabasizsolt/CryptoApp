package com.example.cryptoapp.feature.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.constant.EventConstant
import com.example.cryptoapp.data.model.event.AllEvents
import com.example.cryptoapp.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class EventViewModel(private val repository: EventRepository) : ViewModel() {
    val events = MutableStateFlow<Response<AllEvents>?>(null)

    fun loadAllEvents(page: String = EventConstant.PAGE) {
        viewModelScope.launch {
            val response = repository.getAllEvents(page = page)
            events.value = response
        }
    }

    init {
        loadAllEvents()
    }
}
