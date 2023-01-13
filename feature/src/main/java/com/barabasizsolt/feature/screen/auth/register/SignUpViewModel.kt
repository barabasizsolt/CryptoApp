package com.barabasizsolt.feature.screen.auth.register

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.useCase.RegisterWithEmailAndPasswordUseCase
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SignUpViewModel(private val registerWithEmailAndPasswordUseCase: RegisterWithEmailAndPasswordUseCase) : ViewModel() {

    var email by mutableStateOf(value = "")
        private set
    var password by mutableStateOf(value = "")
        private set
    var screenState by mutableStateOf<ScreenState>(value = ScreenState.Normal)
        private set
    var action by mutableStateOf<Action?>(value = null)
        private set
    val isRegisterEnabled = derivedStateOf { email.isNotEmpty() && password.isNotEmpty() }

    fun registerWithEmailAndPassword() {
        screenState = ScreenState.Loading
        viewModelScope.launch {
            registerWithEmailAndPasswordUseCase(email = email, password = password).onEach { result ->
                screenState = ScreenState.Normal
                when (result) {
                    is AuthResult.Success -> action = Action.NavigateToHome
                    is AuthResult.Failure -> screenState = ScreenState.Error(message = "Registration failed: ${result.error}")
                }
            }.stateIn(scope = this)
        }
    }

    fun onSingInClicked() {
        screenState = ScreenState.Normal
        action = Action.NavigateToLogin
    }

    fun onEmailChange(email: String) {
        this.email = email
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    fun reset() {
        action = null
    }

    sealed class ScreenState {

        object Normal : ScreenState()

        object Loading : ScreenState()

        data class Error(val message: String) : ScreenState()
    }

    sealed class Action {

        object NavigateToHome : Action()

        object NavigateToLogin : Action()
    }
}
