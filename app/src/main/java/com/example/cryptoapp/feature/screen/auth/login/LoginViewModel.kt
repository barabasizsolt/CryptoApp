package com.example.cryptoapp.feature.screen.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackathon.auth.domain.LoginWithEmailAndPasswordUseCase
import com.hackathon.auth.domain.ResetPasswordUseCase
import com.example.cryptoapp.feature.shared.utils.eventFlow
import com.example.cryptoapp.feature.shared.utils.pushEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class LoginViewModel(
    private val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

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

    var isOpen by mutableStateOf(value = false)
    var resetPasswordEmail by mutableStateOf(value = "")

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

    fun resetPassword() {
        resetPasswordUseCase(email = resetPasswordEmail)
        isOpen = false
        resetPasswordEmail = ""
        _event.pushEvent(Event.ShowAfterResetPasswordMessage(message = "An email has been sent to your email address containing a link to reset your password."))
    }

    fun onEmailChange(email: String) {
        resetPasswordEmail = email
    }

    fun onRegisterClicked() {
        _event.pushEvent(Event.NavigateToRegister)
    }

    fun onResetPasswordClicked() {
        _event.pushEvent(Event.ShowResetPasswordDialog)
    }

    sealed class Event {

        data class LoginUser(val nothing: Any? = null) : Event()

        object NavigateToRegister : Event()

        object ShowResetPasswordDialog : Event()

        data class ShowAfterResetPasswordMessage(val message: String) : Event()

        data class ShowErrorMessage(val message: String) : Event()
    }
}
