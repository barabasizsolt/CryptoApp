package com.example.cryptoapp.feature.screen.main.user

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.auth.AuthResult
import com.example.cryptoapp.auth.service.model.User
import com.example.cryptoapp.auth.useCase.GetCurrentUserUseCase
import com.example.cryptoapp.auth.useCase.LogOutUseCase
import com.example.cryptoapp.auth.useCase.UpdateUserUseCase
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    var screenState by mutableStateOf<ScreenState>(value = ScreenState.Loading)
        private set
    var action by mutableStateOf<Action?>(value = null)
        private set
    var user by mutableStateOf<User?>(value = null)
        private set

    init {
        refreshData()
    }

    fun refreshData() {
        screenState = ScreenState.Loading
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                null -> screenState = ScreenState.ShowSnackBar(message = "Unable to get the user's data.")
                else -> {
                    user = result
                    println("URL: $user")
                    screenState = ScreenState.Normal
                }
            }
        }
    }

    fun onNameChange(name: String) {
        user = user?.copy(userName = name)
    }

    fun onLogOutClicked() {
        screenState = ScreenState.LogOutDialog
    }

    fun onDismissClicked() {
        screenState = ScreenState.Normal
    }

    fun updateProfileAvatar(uri: Uri?) {
        user = user?.copy(photo = uri)
        //updateUser()
    }

    fun updateUser() {
        screenState = ScreenState.Loading
        viewModelScope.launch {
            updateUserUseCase(photo = user?.photo, userName = user?.userName.orEmpty()).onEach { result ->
                screenState = when (result) {
                    is AuthResult.Success -> ScreenState.ShowSnackBar(message = "Profile update succeeded.")
                    is AuthResult.Failure -> ScreenState.ShowSnackBar(message = "Profile update failed.")
                }
            }.stateIn(scope = this)
        }
    }

    fun logOutUser() {
        screenState = ScreenState.Loading
        viewModelScope.launch {
            logOutUseCase().onEach { result ->
                when (result) {
                    is AuthResult.Success -> action = Action.NavigateToSignIn
                    is AuthResult.Failure -> screenState = ScreenState.ShowSnackBar(message = "Unable to get the user's data.")
                }
            }.stateIn(scope = this)
        }
    }

    sealed class ScreenState {

        object Normal : ScreenState()

        object Loading : ScreenState()

        data class ShowSnackBar(val message: String) : ScreenState()

        object LogOutDialog : ScreenState()
    }

    sealed class Action {

        object NavigateToResetPassword : Action()

        object NavigateToSignIn : Action()
    }
}
