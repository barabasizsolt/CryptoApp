package com.example.cryptoapp.feature.screen.auth.login

import android.content.Intent
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.useCase.GetIntentForGoogleAccountLoginUseCase
import com.example.cryptoapp.auth.useCase.LoginWithEmailAndPasswordUseCase
import com.example.cryptoapp.auth.useCase.LoginWithGoogleAccountUseCase
import com.example.cryptoapp.auth.useCase.ResetPasswordUseCase
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginWithEmailAndPassword: LoginWithEmailAndPasswordUseCase,
    private val getIntentForGoogleAccountLogin: GetIntentForGoogleAccountLoginUseCase,
    private val loginWithGoogleAccountUseCase: LoginWithGoogleAccountUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    var email by mutableStateOf(value = "")
        private set
    var password by mutableStateOf(value = "")
        private set
    var resetPasswordEmail by mutableStateOf(value = "")
        private set
    var screenState by mutableStateOf<ScreenState>(value = ScreenState.Normal)
        private set
    var action by mutableStateOf<Action?>(value = null)
        private set
    val isLoginEnabled = derivedStateOf { email.isNotEmpty() && password.isNotEmpty() }

    fun loginWithEmailAndPassword() {
        screenState = ScreenState.Loading
        viewModelScope.launch {
            loginWithEmailAndPassword(email = email, password = password).onEach { result ->
                when (result) {
                    is AuthResult.Success -> {
                        action = Action.NavigateToHome
                        screenState = ScreenState.Normal
                    }
                    is AuthResult.Failure -> {
                        screenState = ScreenState.Error(message = "Login failed: ${result.error}")
                    }
                }
            }.stateIn(scope = this)
        }
    }

    fun loginWithGoogle(intent: Intent) {
        screenState = ScreenState.Loading
        viewModelScope.launch {
            loginWithGoogleAccountUseCase(intent = intent).onEach { result ->
                when (result) {
                    is AuthResult.Success -> {
                        action = Action.NavigateToHome
                        screenState = ScreenState.Normal
                    }
                    is AuthResult.Failure -> {
                        screenState = ScreenState.Error(message = "Google Login failed: ${result.error}")
                    }
                }
            }.stateIn(scope = this)
        }
    }

    fun getIntentForGoogleLogin(): Intent = getIntentForGoogleAccountLogin()

    fun reset() {
        action = null
    }

    fun resetPassword() {
        screenState = ScreenState.Loading
        viewModelScope.launch {
            resetPasswordUseCase(email = resetPasswordEmail).onEach { result ->
                screenState = ScreenState.Normal
                resetPasswordEmail = ""
                screenState = when (result) {
                    is AuthResult.Success -> ScreenState.Error(message = "An email has been sent to your email address containing a link to reset your password.")
                    is AuthResult.Failure -> ScreenState.Error(message = "Reset password failed: ${result.error}")
                }
            }.stateIn(scope = this)
        }
    }

    fun onEmailChange(email: String) {
        this.email = email
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    fun onResetPasswordEmailChange(email: String) {
        resetPasswordEmail = email
    }

    fun onRegisterClicked() {
        screenState = ScreenState.Normal
        action = Action.NavigateToRegister
    }

    fun onResetPasswordClicked() {
        screenState = ScreenState.ShowResetPasswordDialog
    }

    fun onDismiss() {
        screenState = ScreenState.Normal
    }

    sealed class ScreenState {

        object Normal : ScreenState()

        object Loading : ScreenState()

        data class Error(val message: String) : ScreenState()

        object ShowResetPasswordDialog : ScreenState()
    }

    sealed class Action {

        object NavigateToHome : Action()

        object NavigateToRegister : Action()
    }
}
