package com.example.cryptoapp.feature.screen.main.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.service.model.User
import com.example.cryptoapp.auth.useCase.GetCurrentUserUseCase
import com.example.cryptoapp.auth.useCase.LogOutUseCase
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.reflect.full.memberProperties

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logOutUseCase: LogOutUseCase
) : ViewModel() {

    var screenState by mutableStateOf<ScreenState>(value = ScreenState.Normal)
        private set
    var action by mutableStateOf<Action?>(value = null)
        private set
    var user by mutableStateOf<User?>(value = null)
        private set

    init {
        screenState = ScreenState.Loading
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                null -> screenState = ScreenState.Error(message = "Unable to get the user's data.")
                else -> {
                    screenState = ScreenState.Normal
                    user = result
                }
            }
        }
    }

    fun onNameChange(name: String) {
        user = user?.copy(userName = name)
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        user = user?.copy(phoneNumber = phoneNumber)
    }

    fun logOutUser() {
        screenState = ScreenState.Loading
        viewModelScope.launch {
            logOutUseCase().onEach { result ->
                when (result) {
                    is AuthResult.Success -> {
                        screenState = ScreenState.Normal
                        action = Action.NavigateToSignIn
                    }
                    is AuthResult.Failure -> screenState = ScreenState.Error(message = "Unable to get the user's data.")
                }
            }.stateIn(scope = this)
        }
    }

    sealed class ScreenState {

        object Normal : ScreenState()

        object Loading : ScreenState()

        data class Error(val message: String) : ScreenState()
    }

    sealed class Action {

        object NavigateToResetPassword : Action()

        object NavigateToSignIn : Action()
    }
}
