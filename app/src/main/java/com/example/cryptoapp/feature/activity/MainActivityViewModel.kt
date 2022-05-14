package com.example.cryptoapp.feature.activity

import androidx.lifecycle.ViewModel
import com.example.cryptoapp.auth.domain.authentication.GetCurrentUserUseCase
import com.example.cryptoapp.data.model.Result
import com.example.cryptoapp.feature.shared.utils.eventFlow
import com.example.cryptoapp.feature.shared.utils.pushEvent
import kotlinx.coroutines.flow.SharedFlow

class MainActivityViewModel(private val getCurrentUserUseCase: GetCurrentUserUseCase) : ViewModel() {

    private val _event = eventFlow<Event>()
    val event: SharedFlow<Event> = _event

    fun getCurrentUser() {
        when (getCurrentUserUseCase()) {
            is Result.Success -> _event.pushEvent(Event.NavigateToMain())
            is Result.Failure -> _event.pushEvent(Event.NavigateToAuthentication())
        }
    }

    sealed class Event {
        data class NavigateToMain(val nothing: Any? = null) : Event()
        data class NavigateToAuthentication(val nothing: Any? = null) : Event()
    }
}
