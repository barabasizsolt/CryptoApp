package com.example.cryptoapp.feature.activity

import androidx.lifecycle.ViewModel
import com.example.cryptoapp.feature.shared.utils.eventFlow
import com.example.cryptoapp.feature.shared.utils.pushEvent
import com.example.cryptoapp.firebase.domain.GetCurrentUserUseCase
import kotlinx.coroutines.flow.SharedFlow

class MainActivityViewModel(private val getCurrentUserUseCase: GetCurrentUserUseCase) : ViewModel() {

    private val _event = eventFlow<Event>()
    val event: SharedFlow<Event> = _event

    fun getCurrentUser() {
        when (getCurrentUserUseCase()) {
            null -> _event.pushEvent(Event.NavigateToAuthentication())
            else -> _event.pushEvent(Event.NavigateToMain())
        }
    }

    sealed class Event {
        data class NavigateToMain(val nothing: Any? = null) : Event()
        data class NavigateToAuthentication(val nothing: Any? = null) : Event()
    }
}
