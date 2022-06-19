package com.example.cryptoapp.wear.screen.auth

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.useCase.GetIntentForGoogleAccountLoginUseCase
import com.example.cryptoapp.auth.useCase.LoginWithEmailAndPasswordUseCase
import com.example.cryptoapp.auth.useCase.LoginWithGoogleAccountUseCase
import com.example.cryptoapp.auth.useCase.ResetPasswordUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun rememberAuthScreenState(
    stateScope: CoroutineScope = rememberCoroutineScope(),
    getIntentForGoogleAccountLogin: GetIntentForGoogleAccountLoginUseCase = get(),
    loginWithGoogleAccountUseCase: LoginWithGoogleAccountUseCase = get(),
    resetPasswordUseCase: ResetPasswordUseCase = get()
): AuthScreenState = rememberSaveable(
    saver = AuthScreenState.getSaver(
        stateScope = stateScope,
        getIntentForGoogleAccountLogin = getIntentForGoogleAccountLogin,
        loginWithGoogleAccountUseCase = loginWithGoogleAccountUseCase,
        resetPasswordUseCase = resetPasswordUseCase
    )
) {
    AuthScreenState(
        stateScope = stateScope,
        getIntentForGoogleAccountLogin = getIntentForGoogleAccountLogin,
        loginWithGoogleAccountUseCase = loginWithGoogleAccountUseCase,
        resetPasswordUseCase = resetPasswordUseCase
    )
}

class AuthScreenState(
    private val stateScope: CoroutineScope,
    private val getIntentForGoogleAccountLogin: GetIntentForGoogleAccountLoginUseCase,
    private val loginWithGoogleAccountUseCase: LoginWithGoogleAccountUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) {
    var screenState by mutableStateOf<ScreenState>(value = ScreenState.Normal)
        private set
    var action by mutableStateOf<Action?>(value = null)
        private set

    fun loginWithGoogle(intent: Intent) {
        screenState = ScreenState.Loading
        stateScope.launch {
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

    sealed class ScreenState {

        object Normal : ScreenState()

        object Loading : ScreenState()

        data class Error(val message: String) : ScreenState()
    }

    sealed class Action {

        object NavigateToHome : Action()

        object NavigateToRegister : Action()
    }

    companion object {

        fun getSaver(
            stateScope: CoroutineScope,
            getIntentForGoogleAccountLogin: GetIntentForGoogleAccountLoginUseCase,
            loginWithGoogleAccountUseCase: LoginWithGoogleAccountUseCase,
            resetPasswordUseCase: ResetPasswordUseCase
        ): Saver<AuthScreenState, *> = mapSaver(
            save = { mapOf() },
            restore = {
                AuthScreenState(
                    stateScope = stateScope,
                    getIntentForGoogleAccountLogin = getIntentForGoogleAccountLogin,
                    loginWithGoogleAccountUseCase = loginWithGoogleAccountUseCase,
                    resetPasswordUseCase = resetPasswordUseCase
                )
            }
        )
    }
}