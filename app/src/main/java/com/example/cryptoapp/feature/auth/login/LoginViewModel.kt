package com.example.cryptoapp.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.domain.authentication.LoginWithEmailAndPasswordUseCase
import com.example.cryptoapp.feature.shared.utils.eventFlow
import com.example.cryptoapp.feature.shared.utils.pushEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class LoginViewModel(private val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    val isLoginEnabled = combine(email, password) {
        email, password ->
        email.isNotEmpty() && password.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _event = eventFlow<Event>()
    val event: SharedFlow<Event> = _event

    fun loginWithEmailAndPassword() {
        _isLoading.value = true
        val result = loginWithEmailAndPasswordUseCase(email = email.value, password = password.value)
        result.addOnSuccessListener {
            _event.pushEvent(Event.LoginUser())
            _isLoading.value = false
        }
        result.addOnFailureListener {
            _event.pushEvent(Event.ShowErrorMessage(message = "Login failed: ${it.message}"))
            _isLoading.value = false
        }
    }

    sealed class Event {

        data class LoginUser(val nothing: Any? = null) : Event()

        data class ShowErrorMessage(val message: String) : Event()
    }
}
