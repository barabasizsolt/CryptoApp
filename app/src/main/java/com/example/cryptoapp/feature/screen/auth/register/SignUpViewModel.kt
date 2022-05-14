package com.example.cryptoapp.feature.screen.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.domain.authentication.RegisterWithEmailAndPasswordUseCase
import com.example.cryptoapp.feature.shared.utils.eventFlow
import com.example.cryptoapp.feature.shared.utils.pushEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

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
            val result = registerWithEmailAndPasswordUseCase(email = email.value, password = password.value)
            result.addOnSuccessListener {
                _event.pushEvent(Event.RegisterUser())
                _isLoading.value = false
            }
            result.addOnFailureListener {
                _event.pushEvent(Event.ShowErrorMessage(message = "Registration failed: ${it.message}"))
                _isLoading.value = false
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
