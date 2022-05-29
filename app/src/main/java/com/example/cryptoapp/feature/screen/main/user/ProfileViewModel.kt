package com.example.cryptoapp.feature.screen.main.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.firebase.data.User
import com.example.cryptoapp.feature.shared.utils.eventFlow
import com.example.cryptoapp.feature.shared.utils.formatUserRegistrationDate
import com.example.cryptoapp.feature.shared.utils.pushEvent
import com.example.cryptoapp.firebase.domain.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ProfileViewModel(getCurrentUserUseCase: GetCurrentUserUseCase) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val user = MutableStateFlow<User?>(null)

    private val _event = eventFlow<Event>()
    val event: SharedFlow<Event> = _event

    private val shouldShowError = MutableStateFlow(false)

    val listItem = combine(user, shouldShowError) { user, shouldShowError ->
        when {
            shouldShowError -> listOf(ProfileListItem.ErrorState())
            user == null -> emptyList()
            else -> listOf(user.toUiModel())
        }
    }

    init {
        viewModelScope.launch {
            _isLoading.value = true
            shouldShowError.value = false
            when (val result = getCurrentUserUseCase()) {
                null -> {
                    shouldShowError.value = true
                    _event.pushEvent(event = Event.ShowErrorMessage(message = "Unable to get the user's data."))
                    _isLoading.value = false
                }
                else -> {
                    user.value = result
                    _isLoading.value = false
                }
            }
        }
    }

    private fun User.toUiModel() = ProfileListItem.User(
        userId = userId,
        avatarType = avatarType,
        email = email,
        registrationDate = registrationTimeStamp.formatUserRegistrationDate()
    )

    sealed class Event {

        object OnChangePasswordClicked : Event()

        object OnSignOutClicked : Event()

        data class ShowErrorMessage(val message: String) : Event()
    }
}
