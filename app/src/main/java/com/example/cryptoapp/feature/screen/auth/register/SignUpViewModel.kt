package com.example.cryptoapp.feature.screen.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.useCase.RegisterWithEmailAndPasswordUseCase
import com.example.cryptoapp.feature.shared.utils.eventFlow
import com.example.cryptoapp.feature.shared.utils.pushEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SignUpViewModel(private val registerWithEmailAndPasswordUseCase: RegisterWithEmailAndPasswordUseCase) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    val isRegisterEnabled = combine(email, password, confirmPassword) {
        email, password, confirmPassword ->
        email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _event = eventFlow<Event>()
    val event: SharedFlow<Event> = _event

    fun registerWithEmailAndPassword() {
        if (validate()) {
            _isLoading.value = true

            viewModelScope.launch {
                registerWithEmailAndPasswordUseCase(email = email.value, password = password.value).onEach { result ->
                    _isLoading.value = false
                    when (result) {
                        is AuthResult.Success -> _event.pushEvent(Event.RegisterUser())
                        is AuthResult.Failure -> _event.pushEvent(Event.ShowErrorMessage(message = "Registration failed: ${result.error}"))
                    }
                }.stateIn(scope = this)
            }
        }
    }

    private fun validate(): Boolean = when (password.value) {
        confirmPassword.value -> true
        else -> false.also { _event.pushEvent(Event.ShowErrorMessage(message = "Passwords doesn't match")) }
    }

    sealed class Event {

        data class RegisterUser(val nothing: Any? = null) : Event()

        data class ShowErrorMessage(val message: String) : Event()
    }
}
