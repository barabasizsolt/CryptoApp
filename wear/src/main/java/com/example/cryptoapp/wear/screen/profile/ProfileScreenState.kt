package com.example.cryptoapp.wear.screen.profile

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
import com.example.cryptoapp.auth.useCase.GetCurrentUserUseCase
import com.example.cryptoapp.auth.useCase.LogOutUseCase
import com.example.cryptoapp.wear.common.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun rememberProfileScreenState(
    stateScope: CoroutineScope = rememberCoroutineScope(),
    getCurrentUserUseCase: GetCurrentUserUseCase = get(),
    logOut: LogOutUseCase = get()
): ProfileScreenState = rememberSaveable(
    saver = ProfileScreenState.getSaver(
        stateScope = stateScope,
        getCurrentUserUseCase = getCurrentUserUseCase,
        logOut = logOut
    )
) {
    ProfileScreenState(
        stateScope = stateScope,
        getCurrentUserUseCase = getCurrentUserUseCase,
        logOut = logOut
    )
}

class ProfileScreenState(
    private val stateScope: CoroutineScope,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logOut: LogOutUseCase
) {

    var action by mutableStateOf<Event<Action>?>(value = null)
        private set
    var user by mutableStateOf<UserUiModel?>(value = null)
        private set
    var screenState by mutableStateOf<ScreenState>(value = ScreenState.Normal)

    init {
        user = getCurrentUserUseCase()?.toUiModel()
    }

    fun onSignOutClicked() {
        screenState = ScreenState.Loading
        stateScope.launch {
            logOut().onEach { result ->
                when (result) {
                    is AuthResult.Success -> action = Event(Action.SignOut)
                    is AuthResult.Failure -> screenState = ScreenState.Error(message = "Unable to log out.")
                }
            }.stateIn(scope = this)
        }
    }

    sealed class ScreenState {
        object Loading: ScreenState()
        object Normal: ScreenState()
        data class Error(val message: String): ScreenState()
    }

    sealed class Action {
        object SignOut : Action()
    }

    companion object {

        private const val USER_KEY: String = "user"

        fun getSaver(
            stateScope: CoroutineScope,
            getCurrentUserUseCase: GetCurrentUserUseCase,
            logOut: LogOutUseCase
        ): Saver<ProfileScreenState, *> = mapSaver(
            save = { mapOf(USER_KEY to it.user) },
            restore = {
                ProfileScreenState(
                    stateScope = stateScope,
                    getCurrentUserUseCase = getCurrentUserUseCase,
                    logOut = logOut
                ).apply {
                    user = it[USER_KEY] as UserUiModel?
                }
            }
        )
    }
}